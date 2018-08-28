package com.chinaums.utils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图片处理工具类
 *
 * Created by liqian
 */
public class ImageUtils {
    /**
     * 传入的图像必须是正方形的 才会 圆形  如果是长方形的比例则会变成椭圆的
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(BufferedImage bimg) throws IOException {
        //透明底的图片
        BufferedImage bi2 = new BufferedImage(bimg.getWidth(),bimg.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0,0,bimg.getWidth(),bimg.getHeight());
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(bimg,0,0,null);
        //设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return bi2;
    }
}
