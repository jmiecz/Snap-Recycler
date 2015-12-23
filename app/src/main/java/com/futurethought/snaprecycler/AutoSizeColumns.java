package com.futurethought.snaprecycler;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.OrientationHelper;

/**
 * Created by Josh Mieczkowski on 12/15/2015.
 */
public class AutoSizeColumns implements Parcelable {
    public static int VERTICAL = OrientationHelper.VERTICAL;
    public static int HORIZONTAL = OrientationHelper.HORIZONTAL;

    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private float percentToShowOfOffViews;
    private int visibleItemCount;
    private int orientation;

    public AutoSizeColumns(Builder builder) {
        this.visibleItemCount = builder.visibleItemCount;
        this.percentToShowOfOffViews = builder.percentToShowOfOffViews;
        this.paddingLeft = builder.paddingLeft;
        this.paddingRight = builder.paddingRight;
        this.paddingTop = builder.paddingTop;
        this.paddingBottom = builder.paddingBottom;
        this.orientation = builder.orientation;
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

    public int getOrientation() {
        return orientation;
    }

    public static class Builder {
        private int paddingLeft = 0;
        private int paddingRight = 0;
        private int paddingTop = 0;
        private int paddingBottom = 0;
        private float percentToShowOfOffViews = 0f;
        private int visibleItemCount = 1;
        private int orientation = HORIZONTAL;

        public Builder setVisibleItemCount(int visibleItemCount) {
            this.visibleItemCount = visibleItemCount;
            return this;
        }

        public Builder setPadding(int padding) {
            this.paddingLeft = padding;
            this.paddingRight = padding;
            this.paddingTop = padding;
            this.paddingBottom = padding;
            return this;
        }

        public Builder setPaddingLeft(int paddingLeft) {
            this.paddingLeft = paddingLeft;
            return this;
        }

        public Builder setPaddiingRight(int paddingRight) {
            this.paddingRight = paddingRight;
            return this;
        }

        public Builder setPaddingTop(int paddingTop) {
            this.paddingTop = paddingTop;
            return this;
        }

        public Builder setPaddingBotton(int paddingBottom) {
            this.paddingBottom = paddingBottom;
            return this;
        }

        public Builder setPercentToShowOfOffViews(float percentToShowOfOffViews) {
            this.percentToShowOfOffViews = minMaxPercent(percentToShowOfOffViews);
            return this;
        }

        public Builder setOrientation(int orientation) {
            if (!(orientation == AutoSizeColumns.VERTICAL || orientation == AutoSizeColumns.HORIZONTAL)) {
                throw new RuntimeException("Value must either be ColumnHandler.VERTICAL or ColumnHandler.HORIZONTAL");
            }
            this.orientation = orientation;
            return this;
        }

        public AutoSizeColumns build() {
            return new AutoSizeColumns(this);
        }

        private static float minMaxPercent(float percent) {
            if (percent > 1f) {
                return 1f;
            } else if (percent < 0f) {
                return 0f;
            } else {
                return percent;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.paddingLeft);
        dest.writeInt(this.paddingRight);
        dest.writeInt(this.paddingTop);
        dest.writeInt(this.paddingBottom);
        dest.writeFloat(this.percentToShowOfOffViews);
        dest.writeInt(this.visibleItemCount);
        dest.writeInt(this.orientation);
    }

    protected AutoSizeColumns(Parcel in) {
        this.paddingLeft = in.readInt();
        this.paddingRight = in.readInt();
        this.paddingTop = in.readInt();
        this.paddingBottom = in.readInt();
        this.percentToShowOfOffViews = in.readFloat();
        this.visibleItemCount = in.readInt();
        this.orientation = in.readInt();
    }

    public static final Parcelable.Creator<AutoSizeColumns> CREATOR = new Parcelable.Creator<AutoSizeColumns>() {
        public AutoSizeColumns createFromParcel(Parcel source) {
            return new AutoSizeColumns(source);
        }

        public AutoSizeColumns[] newArray(int size) {
            return new AutoSizeColumns[size];
        }
    };
}
