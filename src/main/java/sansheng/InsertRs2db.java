package sansheng;

import org.apache.commons.lang.StringUtils;
import sql.SQLHelper;
import utils.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: youxingyang
 * @date: 2018/6/21 14:43
 */
public class InsertRs2db {

    public static void main(String[] args) {
        //File file = new File("C:\\Users\\domainclient\\Desktop\\20180517_040_Taqman_part1.result.tosm.trans.txt");
        //String prefix = "C:\\Users\\domainclient\\Desktop\\新建文本文档.txt";
        String prefix = "E:\\1报告数据\\0水母\\给三生要提供的接口\\位点-优福样本-22850例\\split_file_22850";
        File file;
        String path;
        List<String> result = new ArrayList<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            path = prefix;
            if (i < 10) {
                path += "0" + i;
            } else {
                path += i;
            }
            System.out.println(path);
            file = new File(path);
            if (file.exists()) {
                result.add(insertData(file, i));
            }
        }

        for (String res : result) {
            System.out.println(res);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) / (1000 * 60) + " minutes");
    }

    /**
     * 插入数据
     * @param file
     * @return
     */
    private static String insertData(File file, int index) {
        String result = file.getAbsolutePath();
        List<String> resultList;
        List<String> resultListRes = new ArrayList<>();
        InputStream in;
        try {
            System.out.println("正在读取文件...");
            in = new FileInputStream(file);
            resultList = FileUtil.readFileLine(in);
            if (resultList.size() > 0) {
                System.out.println("读取文件成功");
                String value;
                String[] arr;
                String sampleCode;
                String snp;
                String genoType;
                String dnaAlle;
                String fullName;
                long num = 0;
                long blank = 0;
                long insert = 0;
                long update = 0;
                long less4 = 0;
                String sql;
                // 看看在哪个部分的样本需要忽略
                if (index == 0) {
                    resultListRes = cutResultList(resultList);
                }
                for (int i = 0; i < resultListRes.size(); i++) {
                    value = resultListRes.get(i);
                    if (!StringUtils.isBlank(value)) {
                        if (!value.startsWith("#")) {
                            arr = value.split("\t");
                            if (arr.length == 4) {
                                if (arr[0] != null) {
                                    sampleCode = arr[0].trim();
                                } else {
                                    sampleCode = "";
                                }

                                if (arr[1] != null) {
                                    snp = arr[1].trim();
                                } else {
                                    snp = "";
                                }

                                if (arr[2] != null) {
                                    genoType = arr[2].trim();
                                } else {
                                    genoType = "";
                                }

                                if (arr[3] != null) {
                                    dnaAlle = arr[3].trim();
                                } else {
                                    dnaAlle = "";
                                }

                                //fullName = snp + genoType;

                                if (!StringUtils.isBlank(sampleCode) && !StringUtils.isBlank(snp)) {
                                    if (recordexist(sampleCode, snp)) {
                                        sql = "update t_member_gene_temp_ann set genotype='" + genoType +
                                                "',dna_alle='" + dnaAlle + "' where sample_sn='" + sampleCode + "' and snp = '" + snp + "'";
                                        SQLHelper.execute(sql);
                                        update++;
                                        System.out.println("update done [" + sampleCode + " -> " + snp + "]");
                                    } else {
                                        sql = "insert into t_member_gene_temp_ann (sample_sn,snp,genotype,dna_alle) values (" +
                                                "'" + sampleCode + "','" + snp + "','" + genoType + "','" + dnaAlle + "')";
                                        SQLHelper.execute(sql);
                                        insert++;
                                        System.out.println("insert done [" + sampleCode + " -> " + snp + "]");
                                    }
                                }
                            } else {
                                less4++;
                            }
                        } else {
                            num++;
                        }
                    } else {
                        blank++;
                    }
                }

                if (insert + update == resultList.size() - num - blank - less4) {
                    result += " | success => insert done: " + insert + " update done: " + update;
                } else {
                    result += " | fail => insert: " + insert + " resultList.size(): "
                            + resultList.size() + " #num: " + num
                            + " blank: " + blank + " update: " + update + " less4: " + less4;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result += " | fail => " + e.getMessage() + new Date();
        }
        return result;
    }

    /**
     * 遇到异常情况需要忽略的样本号位点结果
     * @param resultList
     * @return
     */
    private static List<String> cutResultList(List<String> resultList) {
        String[] arr = {"7020-0014-2795","7020-0015-2453","7020-0017-2726","7020-0020-2403","7020-0022-2789","7020-0024-2998","7020-0025-2552","7020-0027-2886","7020-0029-2382","7020-0031-2211","7020-0032-2842","7020-0033-2245","7020-0036-2310","7020-0037-2459","7020-0131-2632","7020-0151-2476","7020-0153-2307","7020-0155-2477","7020-0157-2715","7020-0160-2452","7020-0167-2248","7020-0250-2365","7020-0252-2769","7020-0254-2301","7020-0256-2174","7020-0257-2936","7020-0260-2430","7020-0265-2435","7020-0329-2258","7020-0337-2610","7020-0349-2529","7020-0362-2231","7020-0364-2390","7020-0366-2271","7020-0368-2603","7020-0373-2770","7020-0374-2795","7020-0375-2993","7020-0381-2182","7020-0382-2726","7020-0384-2772","7020-0386-2670","7020-0703-2260","7020-0771-2514","7020-0785-2732","7020-0790-2414","7020-0797-2502","7020-0804-2128","7020-0805-2619","7020-0825-2657","7020-0840-2482","7020-0855-2317","7020-0856-2526","7020-0904-2462","7020-0927-2443","7020-0928-2442","7020-0951-2527","7020-0952-2194","7020-0956-2146","7020-0959-2329","7020-0961-2553","7020-0962-2668","7020-0963-2239","7020-0968-2553","7020-1050-2642","7020-1058-2964","7020-1060-2856","7020-1066-2103","7020-1083-2140","7020-1087-2214","7020-1096-2379","7020-1204-2440","7020-1218-2324","7020-1232-2612","7020-1254-2311","7020-1263-2610","7020-1481-2505","7020-2507-2336","7020-2508-2568","7020-3705-2104","7020-3720-2880","7020-3722-2790","7020-3723-2349","7020-4351-2522","7020-5452-2931","7020-5455-2520","7020-5728-2633","7020-5729-2418","7020-5730-2945","7020-5739-2327","7020-5745-2760","7020-5747-2270","7030-1541-2513","7030-1542-2648","7030-1543-2806","7030-1545-2212","7030-1547-2666","7030-1552-2861","7030-1554-2909","7030-1555-2426","7030-1556-2253","7030-1557-2205","7030-1563-2756","7030-1564-2598","7030-1631-2953","7030-1632-2173","7030-1636-2371","7030-1641-2444","7030-1711-2440","7030-1746-2136","7030-1761-2664","7030-1763-2681","7030-2019-2826","7030-2020-2532","7030-2021-2864","7030-2022-2966","7030-2024-2707","7030-2030-2509","7030-2031-2789","7030-2036-2454","7030-2038-2790","7030-2041-2704","7030-2042-2903","7030-2045-2472","7030-2835-2950","7030-2838-2257","7030-3413-2973","7030-3417-2425","7030-3421-2879","7030-3423-2656","7030-3426-2814","7030-3428-2556","7030-3429-2434","7030-3431-2449","7030-3433-2909","7030-3434-2145","7030-3435-2140","7030-3436-2583","7030-3437-2563","7030-3438-2176","7030-3439-2868","7030-3440-2780","7030-3441-2346","7030-3442-2847","7030-3443-2544","7030-3444-2485","7030-3445-2562","7030-3446-2699","7030-3448-2992","7030-3449-2802","7030-3452-2792","7030-3453-2881","7030-3454-2556","7030-3455-2964","7030-3456-2730","7030-3457-2519","7030-3458-2774","7030-3459-2792","7030-3460-2659","7030-3461-2257","7030-3462-2873","7030-3463-2341","7030-3591-2414","7030-4618-2168","7030-4664-2994","7030-4666-2448","7030-4667-2690","7030-4668-2480","7030-4669-2431","7030-4670-2132","7030-4671-2408","7030-4672-2807","7030-4673-2268","7030-4674-2180","7030-4675-2441","7030-4676-2778","7030-4677-2995","7030-4678-2417","7030-4679-2384","7030-4680-2666","7030-4681-2477","7030-4682-2664","7030-4683-2341","7030-4684-2433","7030-4685-2196","7030-4686-2200","7030-4687-2316","7030-4688-2504","7030-4689-2823","7030-4690-2752","7030-4691-2602","7030-4692-2648","7030-4693-2790","7030-4694-2851","7030-4695-2394","7030-4696-2379","7030-4697-2243","7030-4698-2123","7030-4699-2380","7030-4700-2448","7030-4702-2723","7030-4703-2992","7030-4704-2588","7030-4706-2207","7030-4707-2498","7030-4708-2168","7040-0548-2514","7040-0569-2328","7040-0570-2506","7040-0571-2302","7040-0572-2251","7040-0573-2642","7040-0574-2848","7040-0575-2371","7040-0576-2791","7040-0577-2199","7040-0578-2510","7040-0579-2474","7040-0580-2309","7040-0581-2519","7040-0582-2197","7040-0583-2676","7040-0584-2339","7040-0585-2775","7040-0586-2605","7040-0587-2434","7040-0588-2716","7040-0589-2878","7040-0590-2623","7040-0591-2969","7040-0592-2905","7040-0593-2639","7040-0594-2322","7040-0595-2477","7040-0596-2526","7040-0597-2553","7040-0598-2459","7040-0599-2344","7040-0600-2134","7040-0601-2646","7040-0602-2112","7040-0603-2830","7040-0605-2102","7040-0606-2165","7040-0611-2733","7040-0614-2537","7040-0621-2105","7040-0632-2654","7040-0637-2901","7040-0653-2947","7040-0654-2830","7040-0666-2712","7040-0668-2341","7040-0672-2598","7040-0681-2667","7040-0706-2110","7040-0710-2900","7040-0737-2948","7040-0738-2142","7040-0739-2258","7040-0740-2583","7040-0741-2602","7040-0742-2654","7040-0757-2934","7040-0759-2344","7040-0763-2212","7040-0764-2637","7040-0765-2148","7040-0766-2986","7040-0767-2373","7040-0770-2937","7040-0772-2464","7040-0773-2736","7040-0774-2495","7040-0775-2671","7040-0776-2939","7040-0777-2116","7040-0778-2136","7040-0779-2549","7040-0780-2678","7040-0781-2585","7040-0782-2198","7040-0785-2494","7040-0787-2680","7040-0788-2191","7040-0789-2312","7040-0791-2887","7040-0792-2345","7040-0793-2607","7040-0795-2413","7040-0796-2418","7040-0797-2810","7040-0807-2671","7040-0809-2589","7040-0818-2553","7040-0820-2676","7040-0821-2377","7040-0822-2595","7040-0823-2931","7040-0824-2865","7040-0825-2778","7040-0827-2411","7040-0828-2643","7040-0829-2815","7040-0830-2536","7040-0831-2143","7040-0832-2972","7040-0833-2573","7040-0835-2741","7040-0836-2995","7040-0840-2620","7040-0842-2785","7040-0845-2135","7040-0850-2144","7040-0864-2206","7040-0865-2761","7040-0873-2403","7040-0876-2523","7040-0877-2460","7040-0882-2839","7040-0895-2989","7040-0934-2709","7040-0936-2637","7040-0954-2945","7040-0958-2991","7040-0962-2112","7040-0992-2434","7040-0993-2532","7040-0994-2626","7040-0995-2625","7040-0996-2585","7040-0997-2655","7040-0998-2341","7040-1000-2350","7040-1001-2596","7040-1002-2572","7040-1003-2342","7040-1004-2551","7040-1005-2540","7040-1006-2703","7040-1007-2395","7040-1008-2676","7040-1032-2716","7040-1040-2436","7040-1044-2259","7040-1063-2584","7040-1188-2965","7040-1201-2171","7040-1204-2348","7040-1217-2724","7040-1230-2981","7040-1236-2150","7040-1237-2914","7040-1239-2422","7040-1240-2740","7040-1242-2536","7040-1246-2404","7040-1313-2714","7040-1440-2859","7040-1444-2651","7040-1446-2852","7040-1454-2354","7040-1471-2615","7040-1472-2707","7040-1515-2228","7040-1592-2853","7040-1599-2708","7040-1719-2455","7040-1720-2378","7040-1722-2956","7040-1767-2977","7040-1770-2656","7040-1771-2424","7040-1772-2675","7040-1773-2712","7040-1775-2371","7040-1776-2222","7040-1777-2536","7040-1784-2450","7040-1798-2929","7040-1799-2572","7040-1801-2684","7040-1802-2409","7040-1807-2123","7040-1808-2194","7040-1810-2540","7040-1811-2672","7040-1813-2317","7040-1815-2703","7040-1816-2291","7040-1817-2888","7040-1819-2419","7040-1820-2368","7040-1821-2126","7040-1822-2284","7040-1823-2929","7040-1824-2225","7040-1825-2907","7040-1826-2755","7040-1832-2750","7040-1841-2279","7040-1857-2644","7040-1858-2141","7040-1870-2988","7040-1998-2941","7040-1999-2714","7040-2045-2291","7040-2057-2491","7040-2092-2502","7040-2266-2311","7040-2276-2967","7040-2277-2231","7040-2278-2227","7040-2279-2343","7040-2280-2240","7040-2281-2794","7040-2282-2711","7040-2283-2174","7040-2284-2450","7040-2285-2182","7040-2318-2214","7040-2352-2454","7040-2354-2685","7040-2355-2334","7040-2359-2186","7040-2362-2495","7040-2383-2978","7040-2390-2473","7040-2393-2151","7040-2400-2645","7040-2401-2187","7040-2413-2104","7040-2418-2189","7040-2476-2295","7040-2480-2850","7040-2481-2396","7040-2482-2540","7040-2485-2580","7040-2486-2680","7040-2520-2905","7040-2560-2231","7040-2561-2719","7040-2563-2108","7040-2569-2815","7040-2593-2577","7040-2609-2857","7040-2662-2306","7040-2689-2530","7040-2717-2250","7040-2888-2404","7040-2921-2894","7040-2922-2477","7040-2924-2879","7040-2925-2105","7040-2927-2322","7040-2932-2369","7040-2934-2629","7040-2938-2449","7040-2939-2283","7040-2940-2814","7040-2989-2512","7040-3001-2977","7040-3005-2260","7040-3036-2923","7040-3189-2165","7040-3197-2488","7040-3199-2250","7040-3201-2178","7040-3202-2445","7040-3203-2868","7040-3206-2505","7040-3207-2544","7040-3210-2482","7040-3212-2209","7040-3215-2667","7040-3230-2559","7040-3232-2901","7040-3233-2564","7040-3237-2757","7040-3243-2220","7040-3253-2548","7040-3254-2820","7040-3258-2600","7040-3319-2410","7040-3323-2122","7040-3324-2637","7040-3325-2209","7040-3326-2645","7040-3328-2163","7040-3329-2349","7040-3331-2303","7040-3332-2621","7040-3333-2827","7040-3334-2668","7040-3336-2231","7040-3337-2843","7040-3338-2886","7040-3339-2354","7040-3340-2945","7040-3346-2516","7040-4220-2616","7040-4422-2303","7040-4500-2327","7040-4502-2832","7040-5059-2506","7040-5061-2423","7040-5062-2507","7040-5065-2806","7040-5067-2317","7040-5069-2831","7040-5070-2177","7040-5071-2412","7040-5076-2115","7040-5078-2543","7040-5079-2701","7040-5080-2422","7040-5081-2797","7040-5082-2152","7040-5083-2749","7040-5084-2152","7040-5088-2825","7040-5094-2531","7040-5096-2665","7040-5099-2270","7040-5213-2885","7040-5215-2929","7040-5220-2916","7040-5225-2666","7040-5228-2571","7040-5244-2827","7040-5245-2178","7040-5254-2241","7040-5255-2836","7040-5317-2530","7040-5349-2409","7040-5351-2178","7040-5352-2238","7040-5361-2733","7040-5369-2225","7040-5379-2803","7040-5380-2992","7040-5381-2854","7040-5394-2741","7040-5395-2855","7040-5396-2874","7040-5423-2473","7040-5434-2975","7040-5444-2210","7040-5450-2592","7040-5663-2644","7040-5804-2171","7040-5805-2643","7040-5809-2343","7040-5813-2195","7040-5817-2788","7040-5818-2279","7040-5819-2698","7040-5820-2165","7040-5822-2700","7040-5825-2992","7040-5829-2800","7040-5830-2427","7040-5831-2898","7040-5832-2527","7040-5833-2230","7040-5834-2799","7040-5835-2503","7040-5839-2545","7040-5841-2595","7040-5842-2815","7040-5843-2818","7040-5844-2750","7040-5845-2352","7040-5846-2717","7040-5847-2317","7040-5848-2712","7040-5849-2137","7040-5852-2847","7040-5866-2367","7040-5871-2961","7040-5873-2583","7040-5910-2380","7040-5918-2776","7040-5924-2162","7040-5938-2832","7040-6054-2503","7040-6056-2378","7040-6059-2476","7040-6061-2151","7040-6062-2223","7040-6067-2622","7040-6070-2173","7040-6127-2839","7040-6181-2521","7040-6210-2156"};
        List<String> resultListRes = new ArrayList<>();
        for (String string : resultList) {
            if (!ignore(string, arr)) {
                resultListRes.add(string);
            }
        }
        return resultListRes;
    }

    private static boolean ignore(String string, String[] arr) {
        boolean res = false;
        if (string != null && arr != null) {
            for (String ss : arr) {
                if (string.startsWith(ss)) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 判断记录是否存在
     * @param sampleCode
     * @param snp
     * @return
     */
    private static boolean recordexist(String sampleCode, String snp) {
        boolean result = false;
        try {
            if (!StringUtils.isBlank(sampleCode) && !StringUtils.isBlank(snp)) {
                String select = "select count(sample_sn) from t_member_gene_temp_ann where sample_sn = '" + sampleCode + "' and snp = '" + snp + "'";
                List<?> list1 = SQLHelper.executeQuery(select);
                List<String> result1 = new ArrayList<>();
                if (list1 != null && list1.size() > 0) {
                    for (int i = 0; i < list1.size(); i++) {
                        Object[] temp = (Object[]) list1.get(i);
                        if (temp[0] == null) {
                            continue;
                        }
                        if (!result1.contains(temp[0].toString())) {
                            result1.add(temp[0].toString());
                        }
                    }
                }
                String selectResult = "";
                for (String aResult1 : result1) {
                    selectResult = aResult1;
                }

                int count = Integer.parseInt(selectResult);
                result = count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
