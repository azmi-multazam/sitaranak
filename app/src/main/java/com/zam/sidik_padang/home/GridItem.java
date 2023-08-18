package com.zam.sidik_padang.home;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by supriyadi on 5/5/17.
 */

public class GridItem implements Parcelable {
    public static final Creator<GridItem> CREATOR = new Creator<GridItem>() {
        @Override
        public GridItem createFromParcel(Parcel in) {
            return new GridItem(in);
        }

        @Override
        public GridItem[] newArray(int size) {
            return new GridItem[size];
        }
    };
    public int textId, iconID;

    protected GridItem(Parcel in) {
        textId = in.readInt();
        iconID = in.readInt();
    }

    public GridItem() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(textId);
        dest.writeInt(iconID);
    }
}
