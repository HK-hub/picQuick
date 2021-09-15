package com.cqut.picquick.util;


/**
 * @author : HK意境
 * @ClassName : ImgStringUtil
 * @date : 2021/9/15 9:31
 * @description :
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class ImgStringUtil {

    public static String getFileExtend(String filePath){

        String fileExtend = filePath.substring(filePath.lastIndexOf("."));
        return fileExtend ;
    }

    /**
     * 判断文件是否为图片<br>
     * <br>
     * @param pInput 文件名<br>
     * @param pImgeFlag 判断具体文件类型<br>
     * @return 检查后的结果<br>
     * @throws Exception
     */
    public static boolean legalImages(String  pInput, String pImgeFlag) throws Exception{
        // 文件名称为空的场合
        if(pInput.length() <= 0 || pInput == null || "".equals(pInput)){

            return false;
        }
        // 获得文件后缀名
        String tmpName = getFileExtend(pInput);
        // 声明图片后缀名数组
        String imgeArray [][] = {
                {".bmp", "0"}, {".dib", "1"}, {".gif", "2"},
                {".jfif", "3"}, {".jpe", "4"}, {".jpeg", "5"},
                {".jpg", "6"}, {".png", "7"} ,{".tif", "8"},
                {".tiff", "9"}, {".ico", "10"}
        };
        // 遍历名称数组
        for(int i = 0; i<imgeArray.length;i++){
            // 判断单个类型文件的场合
            if( pImgeFlag != null
                    && imgeArray [i][0].equals(tmpName.toLowerCase())
                    && imgeArray [i][1].equals(pImgeFlag)){
                return true;
            }
            // 判断符合全部类型的场合
            if(pImgeFlag == null
                    && imgeArray [i][0].equals(tmpName.toLowerCase())){
                return true;
            }
        }
        return false;
    }





}
