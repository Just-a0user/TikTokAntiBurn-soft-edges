package made.by.human.tiktokantiburn;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Black rounded rectangle with a soft (feathered) edge.
 *
 * The drawable is meant to be used in a window that is {@code feather} pixels larger than the
 * actual block on every side: the inner rectangle (bounds minus feather) stays fully black and
 * the alpha falls off smoothly to 0 over the {@code feather} pixels outside of it. Because of
 * that the covered area is exactly as big as before, only the transition to the undimmed screen
 * is no longer a hard line.
 *
 * The alpha mask is calculated once per size (signed distance field of a rounded rectangle +
 * smoothstep), so there is no dependency on BlurMaskFilter / software layers.
 */
public class SoftEdgeDrawable extends Drawable {

    private final float feather;
    private final float cornerRadius;
    private final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    @Nullable
    private Bitmap mask;

    /**
     * @param featherPx    width of the soft transition in pixels (0 = hard edge)
     * @param cornerRadius corner radius of the solid core in pixels
     */
    public SoftEdgeDrawable(int featherPx, float cornerRadius) {
        this.feather = Math.max(0, featherPx);
        this.cornerRadius = Math.max(0f, cornerRadius);
        paint.setColor(0xFF000000);
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        if (mask != null) {
            mask.recycle();
            mask = null;
        }
        int w = bounds.width();
        int h = bounds.height();
        if (w <= 0 || h <= 0) return;
        mask = buildMask(w, h);
    }

    private Bitmap buildMask(int w, int h) {
        final float f = Math.max(feather, 0.0001f);

        // half size of the solid core
        final float hw = Math.max(0f, w / 2f - feather);
        final float hh = Math.max(0f, h / 2f - feather);
        final float r = Math.min(cornerRadius, Math.min(hw, hh));

        final float cx = w / 2f;
        final float cy = h / 2f;
        final float innerX = hw - r;
        final float innerY = hh - r;

        byte[] data = new byte[w * h];
        for (int y = 0; y < h; y++) {
            float py = Math.abs(y + 0.5f - cy);
            for (int x = 0; x < w; x++) {
                float px = Math.abs(x + 0.5f - cx);

                // cheap path: clearly inside the solid core
                if (px <= innerX && py <= innerY) {
                    data[y * w + x] = (byte) 255;
                    continue;
                }

                // signed distance to the rounded rectangle (negative inside)
                float qx = px - innerX;
                float qy = py - innerY;
                float ox = Math.max(qx, 0f);
                float oy = Math.max(qy, 0f);
                float d = (float) Math.sqrt(ox * ox + oy * oy) + Math.min(Math.max(qx, qy), 0f) - r;

                float a;
                if (d <= 0f) {
                    a = 1f;
                } else if (d >= f) {
                    a = 0f;
                } else {
                    float t = d / f;
                    float s = t * t * (3f - 2f * t); // smoothstep
                    a = 1f - s;
                }
                data[y * w + x] = (byte) Math.round(a * 255f);
            }
        }

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8);
        bmp.copyPixelsFromBuffer(java.nio.ByteBuffer.wrap(data));
        return bmp;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mask == null) return;
        canvas.drawBitmap(mask, getBounds().left, getBounds().top, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
