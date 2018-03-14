package trendy.mina.com.trendy.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mena on 3/14/2018.
 */

public class Source implements Parcelable {
    private String mName;
    private String mId;

    public Source (String name, String id) {
        mName = name;
        mId = id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    protected Source(Parcel in) {
        mName = in.readString();
        mId = in.readString();
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mId);
    }
}
