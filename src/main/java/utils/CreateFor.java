package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author: youxingyang
 * @date: 2018/10/30 12:40
 */
public class CreateFor {
    public static void main(String[] args) {

        List<String> sampleList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        Map<String, String> productMap = new HashMap<>();

        sampleList.add("028-1111-1495");
        sampleList.add("XS16AP00007");
        map.put("028-1111-1495", "d0dbe880174b44fca7a7c10b88ec0f44");
        map.put("XS16AP00007", "a34c9424c52f464eb2ad109b1bba8abe");
        productMap.put("028-1111-1495", "AN-NK300");
        productMap.put("XS16AP00007", "AN-NK300");
        String storePrefix = "D:/web/awk/parameter/";
        String storeDir = "E:/awk/pdf/nuo300/";
        String url = "http://localhost:8080/annoroad-awk-report/print/toReportCate.shtml";
        String pyPre = "D:/";
        String uuid = "d027ea6bf9764dbc8a0f70b2741b04cd";

        System.out.println(createFor(map, productMap, sampleList, storeDir, url, pyPre, uuid, storePrefix));
    }

    /**
     * 单独生成报告
     * @param map
     * @param productMap
     * @param sampleList
     * @param storeDir
     * @param url
     * @param pyPre
     * @param uuid
     * @param storePrefix
     * @return
     */
    public static int createFor(Map<String, String> map, Map<String, String> productMap,
                         List<String> sampleList, String storeDir, String url, String pyPre, String uuid, String storePrefix) {
        String date = DateUtil.date2Str(new Date());
        StringBuilder pathTemp = new StringBuilder("");
        String companyId;
        String productCode;
        String cmd;
        int sum = 0;
        Map<String, String> sampleMap = new LinkedHashMap<>(sampleList.size());
        try {
            String path;
            for (String sampleCode : sampleList) {
                companyId = map.get(sampleCode);
                productCode = productMap.get(sampleCode);
                pathTemp.append(storeDir).append(date).append("-").append(uuid).append(File.separator).append(companyId).append(File.separator).append(productCode);
                path = pathTemp.toString();
                pathTemp.setLength(0);
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                path += File.separator + sampleCode + "-" + productCode + ".pdf";
                sampleMap.put(sampleCode, path);
            }
            String paraFileName = storePrefix + DateUtil.date2Str(new Date()) + "-" + EncryUtil.getUUID() + ".txt";
            boolean success = FileUtil.writeMapFile(sampleMap, paraFileName);
            if (success) {
                cmd = "python " + pyPre + "create_for.py -u " + url + " -f " + paraFileName;
                Process pr = Runtime.getRuntime().exec(cmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String result = null;
                String line;
                while ((line = in.readLine()) != null) {
                    result = line;
                    System.out.println(result);
                }
                if (result != null && result.contains("completed:")) {
                    sum = Integer.parseInt(result.split(":")[1]);
                    File file = new File(paraFileName);
                    file.delete();
                }
                in.close();
                pr.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sum;
    }
}
