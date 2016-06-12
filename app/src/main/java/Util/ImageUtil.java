package Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by oyj
 */
public class ImageUtil
{
    public static final String DB_NAME = "blurCache";
    public static final String PACKAGE_NAME = "com.bocaiweather.app";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" +
            PACKAGE_NAME + "/" + DB_NAME;


    public ImageUtil(ImageView image, ImageView blurImage){
        applyBlur(image,blurImage);
    }


    //作用：背景虚化
    //参考：http://blog.jobbole.com/63894/
    private void applyBlur(final ImageView image, final ImageView blurImage) {
        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                image.buildDrawingCache();

                Bitmap bmp = image.getDrawingCache();
                blur(bmp,image, blurImage);


                return true;
            }
        });
    }


    /**作用：背景虚化
     *参考：http://blog.jobbole.com/63894/
     *bkg:要虚化的图片
     *view:要虚化的范围 * */
    private void blur(Bitmap bkg, View view, ImageView blurImage) {
        long startMs = System.currentTimeMillis();
        float scaleFactor = 8;//scaleFactor提供了需要缩小的等
        float radius = 2;


        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);//进行双缓冲
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        blurImage.setImageBitmap(saveBitmap2file(overlay,DB_PATH));
        // view.setBackground(new BitmapDrawable(getResources(), overlay));

    }

    /**把图片转为文件，这么做的原因是为了优化recycleView滑动时的卡顿*/
    public static Bitmap saveBitmap2file(Bitmap bmp,String DB_PATH)
    {
        Bitmap blurredBitmap;
        int quality = 100;
        OutputStream stream = null;
        try
        {
            stream = new FileOutputStream(DB_PATH);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        bmp.compress(Bitmap.CompressFormat.PNG, quality, stream);
        blurredBitmap = BitmapFactory.decodeFile(DB_PATH);
        return blurredBitmap;
    }

}
