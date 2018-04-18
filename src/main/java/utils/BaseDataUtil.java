package utils;

/**
 * @Author: youxingyang
 * @date: 2018/4/17 13:18
 */
public final class BaseDataUtil {
    private BaseDataUtil() {
    }

    /**根据协议在发送网络流的时候需要将所有int及long转换成byte**/
    /**
     * int 2 byte[]
     * @param num
     * @return
     */
    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ix++) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * byte[] 2 int
     * @param byteNum
     * @return
     */
    public static int bytes2Int(byte[] byteNum) {
        int num = 0;
        for (int ix = 0; ix < 4; ix++) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    /**
     * int 2 byte
     * @param num
     * @return
     */
    public static byte int2OneByte(int num) {
        return (byte) (num & 0x000000ff);
    }

    /**
     * byte 2 int
     * @param byteNum
     * @return
     */
    public static int oneByte2Int(byte byteNum) {
        return byteNum > 0 ? byteNum : (128 + (128 + byteNum));
    }

    /**
     * long 2 byte[]
     * @param num
     * @return
     */
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ix++) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * byte[] 2 long
     * @param byteNum
     * @return
     */
    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ix++) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

}