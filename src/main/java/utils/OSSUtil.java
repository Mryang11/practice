package utils;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: youxingyang
 * @date: 2018/9/26 17:47
 */
@Slf4j
public final class OSSUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;

    private static OSS client = null;

    private static String bucketName;
    private static String key;

    public OSSUtil(String endpoint, String accessKeyId, String accessKeySecret, String bucketName1, String key1) {
        if (OsUtil.isWindows()) {
            this.endpoint = endpoint.replace("-internal", "");
        } else {
            this.endpoint = endpoint;
        }
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        bucketName = bucketName1;
        key = key1;
    }

    /**
     * 分片上传文件至OSS
     * @param localFilePath     本地文件
     * @return
     */
    public boolean multipartUploadFile(String localFilePath) throws ExecutionException, InterruptedException {
        boolean res = false;
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setIdleConnectionTime(1000);
        client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, conf);

        try {
            /*
             * Claim a upload id firstly
             */
            String uploadId = claimUploadId();
            log.info("Claiming a new upload id " + uploadId + "\n");

            /*
             * Calculate how many parts to be divided
             */
            // 10MB
            final long partSize = 5 * 1024 * 1024L;
            final File sampleFile = new File(localFilePath);
            long fileLength = sampleFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount++;
            }
            if (partCount > 10000) {
                throw new RuntimeException("Total parts count should not exceed 10000");
            } else {
                log.info("Total parts count " + partCount + "\n");
            }

            /*
             * Upload multiparts to your bucket
             */
            log.info("Begin to upload multiparts to OSS from a file\n");
            List<Future<PartETag>> tmpList = Collections.synchronizedList(new ArrayList<>(partCount));
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
                tmpList.add(executorService.submit(new PartUploader(sampleFile, startPos, curPartSize, i + 1, uploadId)));
            }

            /*
             * Waiting for all parts finished
             */
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                try {
                    //executorService.awaitTermination(5, TimeUnit.SECONDS);
                    executorService.awaitTermination(1, TimeUnit.DAYS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            List<PartETag> partETags = Collections.synchronizedList(new ArrayList<PartETag>());
            for (Future<PartETag> aTmpList : tmpList) {
                if (aTmpList.isDone()) {
                    partETags.add(aTmpList.get());
                }
            }

            /*
             * Verify whether all parts are finished
             */
            if (partETags.size() != partCount) {
                throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
            } else {
                log.info("Succeed to complete multiparts into an object named " + key + "\n");
                res = true;
            }

            /*
             * View all parts uploaded recently
             */
            listAllParts(uploadId);

            /*
             * Complete to upload multiparts
             */
            completeMultipartUpload(uploadId, partETags);

            /*
             * Fetch the object that newly created at the step below.
             */
            log.info("Fetching an object");
            client.getObject(new GetObjectRequest(bucketName, key), new File(localFilePath));
            res = true;
        } catch (OSSException oe) {
            log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.info("Error Message: " + oe.getErrorCode());
            log.info("Error Code:       " + oe.getErrorCode());
            log.info("Request ID:      " + oe.getRequestId());
            log.info("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            log.info("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.info("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            if (client != null) {
                client.shutdown();
            }
        }
        return res;
    }

    private static class PartUploader implements Callable<PartETag> {

        private File localFile;
        private long startPos;

        private long partSize;
        private int partNumber;
        private String uploadId;

        PartUploader(File localFile, long startPos, long partSize, int partNumber, String uploadId) {
            this.localFile = localFile;
            this.startPos = startPos;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.uploadId = uploadId;
        }

        public PartETag call() {
            PartETag partETag = null;
            InputStream instream = null;
            try {
                instream = new FileInputStream(this.localFile);
                instream.skip(this.startPos);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(this.uploadId);
                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setPartSize(this.partSize);
                uploadPartRequest.setPartNumber(this.partNumber);

                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                log.info("Part#" + this.partNumber + " done\n");
                partETag = uploadPartResult.getPartETag();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return partETag;
        }
    }

    private static String claimUploadId() {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult result = client.initiateMultipartUpload(request);
        return result.getUploadId();
    }

    private static void completeMultipartUpload(String uploadId, List<PartETag> partETags) {
        // Make part numbers in ascending order
        partETags.sort(Comparator.comparingInt(PartETag::getPartNumber));

        log.info("Completing to upload multiparts\n");
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);
        client.completeMultipartUpload(completeMultipartUploadRequest);
    }

    private static void listAllParts(String uploadId) {
        log.info("Listing all parts......");
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, key, uploadId);
        PartListing partListing = client.listParts(listPartsRequest);

        int partCount = partListing.getParts().size();
        for (int i = 0; i < partCount; i++) {
            PartSummary partSummary = partListing.getParts().get(i);
            log.info("\tPart#" + partSummary.getPartNumber() + ", ETag=" + partSummary.getETag());
        }
        System.out.println();
    }

}
