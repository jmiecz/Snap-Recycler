package com.snaprecycler;

/**
 * Created by Josh Mieczkowski on 12/15/2015.
 */
public class ColumnHandler {
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private float percentToShowOfOffViews = 0f;
    private int visibleItemCount = 1;

    public ColumnHandler(Builder builder) {
        this.visibleItemCount = builder.visibleItemCount;
        this.percentToShowOfOffViews = builder.percentToShowOfOffViews;
        this.paddingLeft = builder.paddingLeft;
        this.paddingRight = builder.paddingRight;
        this.paddingTop = builder.paddingTop;
        this.paddingBottom = builder.paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public float getPercentToShowOfOffViews() {
        return percentToShowOfOffViews;
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    public static class Builder{
        private int paddingLeft = 0;
        private int paddingRight = 0;
        private int paddingTop = 0;
        private int paddingBottom = 0;
        private float percentToShowOfOffViews = 0f;
        private int visibleItemCount = 1;

        public Builder setVisibleItemCount(int visibleItemCount){
            this.visibleItemCount = visibleItemCount;
            return this;
        }

        public Builder setPadding(int padding){
            this.paddingLeft = padding;
            this.paddingRight = padding;
            this.paddingTop = padding;
            this.paddingBottom = padding;
            return this;
        }

        public Builder setPaddingLeft(int paddingLeft){
            this.paddingLeft = paddingLeft;
            return this;
        }

        public Builder setPaddiingRight(int paddingRight){
            this.paddingRight = paddingRight;
            return this;
        }

        public Builder setPaddingTop(int paddingTop){
            this.paddingTop = paddingTop;
            return this;
        }

        public Builder setPaddingBotton(int paddingBottom){
            this.paddingBottom = paddingBottom;
            return this;
        }

        public Builder setPercentToShowOfOffViews(float percentToShowOfOffViews){
            this.percentToShowOfOffViews = minMaxPercent(percentToShowOfOffViews);
            return this;
        }

        public ColumnHandler build(){
            return new ColumnHandler(this);
        }

        private static float minMaxPercent(float percent){
            if(percent > 1f){
                return 1f;
            }else if(percent < 0f){
                return 0f;
            }else{
                return percent;
            }
        }
    }
}
