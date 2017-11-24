package org.lff.handwriting;

/**
 * @author Feifei Liu
 * @datetime 2017-11-20  15:34
 */
public class Option {

    private String lineColor;
    private LineStyle lineStyle;
    private String textColor;

    private Option() {

    }

    public static Option getInstance() {
        return HOLDER.instance;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineStyle(LineStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    public LineStyle getLineStyle() {
        return lineStyle;
    }

    static private class HOLDER {
        static Option instance = new Option();
    }

    private int rowCount = 12;
    private int leftOffset = 36;
    private int rightOffset = 36;
    private int right = 570;
    private int topOffset = 36;
    private int bottomOffset = 36;
    private int rowGap = 36;

    private boolean addEmptyLineAfter = false;

    public boolean isSkipEmptyLine() {
        return skipEmptyLine;
    }

    public void setSkipEmptyLine(boolean skipEmptyLine) {
        this.skipEmptyLine = skipEmptyLine;
    }

    private boolean skipEmptyLine = true;

    public boolean isAddEmptyLineAfter() {
        return addEmptyLineAfter;
    }

    public void setAddEmptyLineAfter(boolean addEmptyLineAfter) {
        this.addEmptyLineAfter = addEmptyLineAfter;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getLeftOffset() {
        return leftOffset;
    }

    public void setLeftOffset(int leftOffset) {
        this.leftOffset = leftOffset;
    }

    public int getRightOffset() {
        return rightOffset;
    }

    public void setRightOffset(int rightOffset) {
        this.rightOffset = rightOffset;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTopOffset() {
        return topOffset;
    }

    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }

    public int getBottomOffset() {
        return bottomOffset;
    }

    public void setBottomOffset(int bottomOffset) {
        this.bottomOffset = bottomOffset;
    }

    public int getRowGap() {
        return rowGap;
    }

    public void setRowGap(int rowGap) {
        this.rowGap = rowGap;
    }

    public float getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }

    public String getTextColor() {
        return this.textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Option{");
        sb.append("lineColor='").append(lineColor).append('\'');
        sb.append(", lineStyle=").append(lineStyle);
        sb.append(", textColor='").append(textColor).append('\'');
        sb.append(", rowCount=").append(rowCount);
        sb.append(", leftOffset=").append(leftOffset);
        sb.append(", rightOffset=").append(rightOffset);
        sb.append(", right=").append(right);
        sb.append(", topOffset=").append(topOffset);
        sb.append(", bottomOffset=").append(bottomOffset);
        sb.append(", rowGap=").append(rowGap);
        sb.append(", addEmptyLineAfter=").append(addEmptyLineAfter);
        sb.append(", skipEmptyLine=").append(skipEmptyLine);
        sb.append(", cellHeight=").append(cellHeight);
        sb.append('}');
        return sb.toString();
    }

    private float cellHeight = 12;
}
