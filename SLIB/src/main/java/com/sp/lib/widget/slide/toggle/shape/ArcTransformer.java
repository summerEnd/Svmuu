package com.sp.lib.widget.slide.toggle.shape;

public class ArcTransformer extends ShapeTransformer<ArcShape> {
    public ArcTransformer(ArcShape shapeStart, ArcShape shapeEnd) {
        super(shapeStart, shapeEnd);
    }

    @Override
    public void onPrepareOutput(ArcShape out, ArcShape start, ArcShape end, float ratio) {
        out.startAngle = getValue(ratio, start.startAngle, end.startAngle);
        out.sweepAngle = getValue(ratio, start.sweepAngle, end.sweepAngle);
        out.rectF.set(
                getValue(ratio, start.rectF.left, end.rectF.left),
                getValue(ratio, start.rectF.top, end.rectF.top),
                getValue(ratio, start.rectF.right, end.rectF.right),
                getValue(ratio, start.rectF.bottom, end.rectF.bottom)
        );
    }

    @Override
    protected ArcShape generateShape() {
        return new ArcShape();
    }
}
