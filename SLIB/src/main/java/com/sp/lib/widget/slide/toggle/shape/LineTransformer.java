package com.sp.lib.widget.slide.toggle.shape;

public class LineTransformer extends ShapeTransformer<SLine> {

    public LineTransformer(SLine shapeStart, SLine shapeEnd) {
        super(shapeStart, shapeEnd);
    }

    @Override
    public void onPrepareOutput(SLine outputShape, SLine start, SLine end, float ratio) {
        outputShape.startX = getValue(ratio, start.startX, end.startX);
        outputShape.endX = getValue(ratio, start.endX, end.endX);
        outputShape.startY = getValue(ratio, start.startY, end.startY);
        outputShape.endY = getValue(ratio, start.endY, end.endY);
    }

    @Override
    protected SLine generateShape() {
        return new SLine();
    }
}
