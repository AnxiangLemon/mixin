package com.lemon.mixin.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class QRCodeUtils {
    private static final int QRCOLOR = 0xFF000000; // 默认是黑色
    private static final int BGWHITE = 0xFFFFFFFF; // 背景颜色

    private static final int WIDTH = 300; // 二维码宽
    private static final int HEIGHT = 300; // 二维码高

    private static final String IMG_FORMAT = "png";

    // 用于设置QR二维码参数
    private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
        private static final long serialVersionUID = 1L;

        {
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
            put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置编码方式
            put(EncodeHintType.MARGIN, 2);
        }
    };


    /**
     *
     * @Description 生成制定内容的二维码 保存到指定路径
     * @Author AnxiangLemon
     * @Date 2018/10/26 13:11
     * @Param [filePath:路径, content:二维码内容]
     * @Return void
     */
    public void createQRCode(String filePath, String content) {
        /**
         * 生成二维码
         */
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            Path file = new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, IMG_FORMAT, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 识别二维码内容
     * @Author AnxiangLemon
     * @Date 2018/10/26 13:06
     * @Param [filePath]
     * @Return java.lang.String
     */
    public String getContentFromQRCode(String filePath) {
        MultiFormatReader formatReader = new MultiFormatReader();
        File file = new File(filePath);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer
                    (new BufferedImageLuminanceSource(image)));
            HashMap hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");    //指定字符编码为“utf-8”
            Result result = formatReader.decode(binaryBitmap, hints);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @Description  生成带logo的二维码图片
     * @Author AnxiangLemon
     * @Date 2018/10/26 12:56
     * @Param [logoFile, codeFile, qrUrl, note]
     * @Return void
     */
    public static void drawLogoQRCode(File logoFile, String filePath, String qrUrl, String note) {

        try {
            File codeFile = new File(filePath);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bm = multiFormatWriter.encode(qrUrl, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }

            int width = image.getWidth();
            int height = image.getHeight();
            if (Objects.nonNull(logoFile) && logoFile.exists()) {
                // 构建绘图对象
                Graphics2D g = image.createGraphics();
                // 读取Logo图片
                BufferedImage logo = ImageIO.read(logoFile);
                // 开始绘制logo图片
                g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
                g.dispose();
                logo.flush();
            }

            // 自定义文本描述
            if (StringUtils.isNotEmpty(note)) {
                // 新的图片，把带logo的二维码下面加上文字
                BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg = outImage.createGraphics();
                // 画二维码到新的面板
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                // 画文字到新的面板
                outg.setColor(Color.BLACK);
                outg.setFont(new Font("楷体", Font.BOLD, 30)); // 字体、字型、字号
                int strWidth = outg.getFontMetrics().stringWidth(note);
                if (strWidth > 399) {
                    // //长度过长就截取前面部分
                    // 长度过长就换行
                    String note1 = note.substring(0, note.length() / 2);
                    String note2 = note.substring(note.length() / 2, note.length());
                    int strWidth1 = outg.getFontMetrics().stringWidth(note1);
                    int strWidth2 = outg.getFontMetrics().stringWidth(note2);
                    outg.drawString(note1, 200 - strWidth1 / 2, height + (outImage.getHeight() - height) / 2 + 12);
                    BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D outg2 = outImage2.createGraphics();
                    outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                    outg2.setColor(Color.BLACK);
                    outg2.setFont(new Font("宋体", Font.BOLD, 30)); // 字体、字型、字号
                    outg2.drawString(note2, 200 - strWidth2 / 2, outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
                    outg2.dispose();
                    outImage2.flush();
                    outImage = outImage2;
                } else {
                    outg.drawString(note, 200 - strWidth / 2, height + (outImage.getHeight() - height) / 2 + 12); // 画文字
                }
                outg.dispose();
                outImage.flush();
                image = outImage;
            }

            image.flush();

            ImageIO.write(image, IMG_FORMAT, codeFile); // TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
