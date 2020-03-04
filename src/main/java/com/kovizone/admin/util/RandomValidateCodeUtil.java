package com.kovizone.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 随机验证码工具
 *
 * @author unknow
 */
public class RandomValidateCodeUtil {

    private static final Logger logger = LoggerFactory.getLogger(RandomValidateCodeUtil.class);

    /**
     * 放到session中的key
     */
    public static final String RANDOM_VALIDATE_CODE_KEY = "RANDOM_VALIDATE_CODE_KEY";

    /**
     * 随机值因子
     * 去除I、L、Z
     */
    private static final String RANDOM_FACTORY = "0123456789ABCDEFGHJKMNPQRSTUVWXY";

    /**
     * 图片宽
     */
    private static final int IMAGE_WIDTH = 95;

    /**
     * 图片高
     */
    private static final int IMAGE_HEIGHT = 25;

    /**
     * 干扰线数量
     */
    private static final int DISTURB_LINE_SIZE = 40;

    /**
     * 随机产生字符数量
     */
    private static final int CODE_LENGTH = 4;

    private static Random random = new Random();

    /**
     * 获得字体
     */
    private static Font getFont() {
        return new Font("Fixedsys", Font.BOLD, 18);
    }

    /**
     * 获得颜色
     */
    private static Color getRandColor() {
        int fc = 110;
        int bc = 133;
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 校验验证码<BR/>
     * 忽略近似字符
     *
     * @param verify 用户输入验证码
     * @param random 系统生成验证码值
     * @return 校验结果
     */
    public static boolean verifyRandomCode(String verify, String random) {
        if (verify.replace("o", "0")
                .replace("O", "0")
                .replace("z", "2")
                .replace("Z", "2")
                .replace("i", "1")
                .replace("I", "1")
                .replace("l", "1")
                .replace("L", "1")
                .equalsIgnoreCase(random)) {
            return true;
        }
        return false;
    }

    /**
     * 生成随机图片
     */
    public static void getRandomCode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_BGR);
        // 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        Graphics g = image.getGraphics();
        //图片大小
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        //字体大小
        g.setFont(new Font("Default", Font.PLAIN, 18));
        //字体颜色
        g.setColor(getRandColor());
        // 绘制干扰线
        for (int i = 0; i <= DISTURB_LINE_SIZE; i++) {
            drawLine(g);
        }
        // 绘制随机字符
        String randomString = "";
        for (int i = 1; i <= CODE_LENGTH; i++) {
            randomString = drawString(g, randomString, i);
        }
        logger.info(randomString);
        //将生成的随机字符串保存到session中
        session.removeAttribute(RANDOM_VALIDATE_CODE_KEY);
        session.setAttribute(RANDOM_VALIDATE_CODE_KEY, randomString);
        g.dispose();
        try {
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 绘制字符串
     */
    private static String drawString(Graphics g, String randomString, int i) {
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
                .nextInt(121)));
        String rand = getRandomString(random.nextInt(RANDOM_FACTORY.length()));
        randomString += rand;
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(rand, 13 * i, 16);
        return randomString;
    }

    /**
     * 绘制干扰线
     */
    private static void drawLine(Graphics g) {
        int x = random.nextInt(IMAGE_WIDTH);
        int y = random.nextInt(IMAGE_HEIGHT);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 获取随机的字符
     */
    public static String getRandomString(int num) {
        return String.valueOf(RANDOM_FACTORY.charAt(num));
    }
}
