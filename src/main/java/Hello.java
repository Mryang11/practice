import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipOutputStream;
import utils.DateUtil;
import utils.OSSUtil;
import utils.PropertiesUtil;
import utils.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author: youxingyang
 * @date: 2018/8/9 8:27
 */
@Data
@Slf4j
public class Hello {
    private Integer id;
    private String name;
    private String password;

    private static String STORE_PREFIX = "D:/awk/";
    private static String BUCKET_PREFIX = "pdf/nuo300/";
    private static String STORE_DIR = STORE_PREFIX + BUCKET_PREFIX;
    private static String BEAM = "-";
    private static String VIRGULE = "/";
    private static String URL = "http://localhost:8080/annoroad-awk-report/print/toReportCate.shtml";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        String bucketName = PropertiesUtil.readProperties("config.properties", "bucketName");
        String endpoint = PropertiesUtil.readProperties("config.properties", "endpoint");
        String accessKeyId = PropertiesUtil.readProperties("config.properties", "accessKeyId");
        String accessKeySecret = PropertiesUtil.readProperties("config.properties", "accessKeySecret");
        String ossUrl = "http://" + bucketName + "." + endpoint.replace("-internal", "");
        String uuid = "d027ea6bf9764dbc8a0f70b2741b04cd";
        String current = STORE_DIR + DateUtil.date2Str(new Date()) + BEAM + uuid;
        File file = new File(current);
        File[] companyFiles = file.listFiles();
        if (companyFiles != null) {
            // 各公司
            String companyId;
            for (File companyFile : companyFiles) {
                if (companyFile != null) {
                    companyId = companyFile.getName();
                    File[] productFiles = companyFile.listFiles();
                    if (productFiles != null) {
                        // 各产品代码
                        String code;
                        String tempZipName;
                        String key;
                        boolean success;
                        int companyPdfCount;
                        File[] file2Arr;
                        String zipName;
                        for (File productFile : productFiles) {
                            code = productFile.getName();
                            file2Arr = productFile.listFiles();
                            if (file2Arr != null) {
                                companyPdfCount = file2Arr.length;
                                tempZipName = companyId + "_" + companyPdfCount + "_" + code + "_" + DateUtil.date2Str(new Date()) + BEAM + uuid + ".zip";
                                zipName = productFile.getParentFile().getAbsolutePath() + VIRGULE + tempZipName;
                                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipName));
                                ZipUtil.zip(out, productFile, code);
                                out.close();

                                // 上传至OSS
                                key = BUCKET_PREFIX + DateUtil.date2Str(new Date()) + BEAM + uuid + VIRGULE + companyId + VIRGULE + tempZipName;
                                OSSUtil ossUtil = new OSSUtil(endpoint, accessKeyId, accessKeySecret, bucketName, key);
                                success = ossUtil.multipartUploadFile(zipName);
                                if (success) {
                                    log.info("上传" + zipName + "成功");
                                } else {
                                    log.info("上传" + zipName + "失败");
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public int getLastNumIndex(String string) {
        char[] chars = string.toCharArray();
        int index = -1;
        for (char c : chars) {
            if ((c + "").matches("\\d")) {
                index++;
            }
        }
        return index;
    }


}
