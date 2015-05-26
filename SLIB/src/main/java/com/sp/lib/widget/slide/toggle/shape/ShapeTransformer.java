package com.sp.lib.widget.slide.toggle.shape;

/**
 * 基本图形转换
 */
public abstract class ShapeTransformer<S> {
    /**
     * 输出的形状，根据当前比例来计算
     */
    private S outputShape;
    /**
     * 初始形状
     */
    private S shapeStart;
    /**
     * 结束形状
     */
    private S shapeEnd;

    public ShapeTransformer(S shapeStart, S shapeEnd) {
        this.shapeStart = shapeStart;
        this.shapeEnd = shapeEnd;
        outputShape = generateShape();
    }

    public final S outPut(float ratio) {
        onPrepareOutput(outputShape, shapeStart, shapeEnd, ratio);
        return outputShape;
    }

    /**
     * 当调用outPut时，会回调这个方法，用来计算新的图形
     *
     * @param output 输出图形
     * @param start       初始图形
     * @param end         结束图形
     * @param ratio       比例 0-1,0开始，1结束
     */
    public abstract void onPrepareOutput(S output, S start, S end, float ratio);

    /**
     * 生成默认形状用来输出
     */
    protected abstract S generateShape();

    public S getShapeStart() {
        return shapeStart;
    }

    public S getShapeEnd() {
        return shapeEnd;
    }

    public float getValue(float ratio, float start, float end) {
        return start + (end - start) * ratio;
    }

}
