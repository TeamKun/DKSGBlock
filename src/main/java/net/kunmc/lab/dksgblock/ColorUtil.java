package net.kunmc.lab.dksgblock;

import java.util.function.Function;


public class ColorUtil {

    /**
     * 16進数カラーコードから赤色を取得
     * 255(FF)が最大
     *
     * @param color カラーコード
     * @return 赤色
     */
    public static int getRed(int color) {
        return (color >> 16) & 0xFF;
    }

    /**
     * 16進数カラーコードから緑色を取得
     * 255(FF)が最大
     *
     * @param color カラーコード
     * @return 緑色
     */
    public static int getGreen(int color) {
        return (color >> 8) & 0xFF;
    }

    /**
     * 16進数カラーコードから青色を取得
     * 255(FF)が最大
     *
     * @param color カラーコード
     * @return 青色
     */
    public static int getBlue(int color) {
        return color & 0xFF;
    }

    /**
     * 16進数ARGBカラーコードから透過率を取得
     * 255(FF)が最大
     * 0に近いほど透明
     *
     * @param color カラーコード
     * @return 透過率
     */
    public static int getAlpha(int color) {
        return color >>> 24;
    }

    /**
     * RGBから16進数カラーコードへ変換
     *
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @return カラーコード
     */
    public static int getHexColor(int r, int g, int b) {
        return (r << 16) + (g << 8) + b;
    }

    /**
     * RGBAから16進数ARGBカラーコードへ変換
     *
     * @param r 赤
     * @param g 緑
     * @param b 青
     * @param a 透過度 ０に近いほど透明
     * @return カラーコード
     */
    public static int getARGBHexColor(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    /**
     * 数字カラーコードを文字列の16新数のカラーコードに変換
     *
     * @param colorCode カラーコード
     * @return カラーコード文字列
     */
    public static String getStringHexColor(int colorCode) {
        String hex = Integer.toHexString(colorCode);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.max(0, 6 - hex.length()); i++) {
            sb.append("0");
        }
        sb.append(hex);
        return sb.toString();
    }

    /**
     * 色ごとの距離を取得
     * 16新数のカラーコード
     *
     * @param color1 色1
     * @param color2 色2
     * @return 距離
     */
    public static double getColorDistance(int color1, int color2) {
        return Math.abs( Math.pow(getAlpha(color1) - getAlpha(color2), 2)+Math.sqrt(Math.pow(getRed(color1) - getRed(color2), 2) + Math.pow(getGreen(color1) - getGreen(color2), 2) + Math.pow(getBlue(color1) - getBlue(color2), 2)));
    }

    /**
     * 近似色を取得する
     * 16新数のカラーコード
     *
     * @param target 対象色
     * @param colors 比較色
     * @return 比較色の中で最も近い色
     */
    public static int getApproximateColor(int target, int... colors) {
        int most = -1;
        double mostDis = Double.MAX_VALUE;
        for (int color : colors) {
            if (target == color)
                return color;
            double dis = getColorDistance(target, color);
            if (dis < mostDis) {
                mostDis = dis;
                most = color;
            }
        }
        return most;
    }

    /**
     * 近似色のオブジェクトを取得する
     * 16新数のカラーコード
     *
     * @param target      対象色
     * @param colors      比較色オブジェクト
     * @param colorGetter 比較色取得
     * @param <T>         比較オブジェクト
     * @return 比較色の中で最も近い色のオブジェクト
     */
    public static <T> T getApproximateColorObject(int target, T[] colors, Function<T, Integer> colorGetter) {
        T mostObject = null;
        double mostDis = Double.MAX_VALUE;
        for (T color : colors) {
            int col = colorGetter.apply(color);
            if (target == col)
                return color;
            double dis = getColorDistance(target, col);
            if (dis < mostDis) {
                mostDis = dis;
                mostObject = color;
            }
        }
        return mostObject;
    }

    /**
     * 色を平均的に混ぜた色を取得する
     *
     * @param colors 混合する色
     * @return 混合した色
     */
    public static int getAverageMixColor(int... colors) {
        double r = 0, g = 0, b = 0;
        for (int color : colors) {
            r += getRed(color);
            g += getGreen(color);
            b += getBlue(color);
        }
        return getHexColor((int) (r / colors.length), (int) (g / colors.length), (int) (b / colors.length));
    }
}