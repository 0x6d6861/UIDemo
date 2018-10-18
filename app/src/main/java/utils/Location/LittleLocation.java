package utils.Location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LittleLocation
        implements Parcelable
{
    public static final Parcelable.Creator<LittleLocation> CREATOR = new Parcelable.Creator()
    {
        public LittleLocation createFromParcel(Parcel paramAnonymousParcel)
        {
            return new LittleLocation(paramAnonymousParcel);
        }

        public LittleLocation[] newArray(int paramAnonymousInt)
        {
            return new LittleLocation[paramAnonymousInt];
        }
    };
    private final float mAccuracy;
    private final double mAltitude;
    private final float mBearing;
    private final float mSpeed;
    private final long mTime;
    private final LittleLatLng mLittleLocationLatLng;

    public LittleLocation(float paramFloat1, double paramDouble, float paramFloat2, float paramFloat3, long paramLong, LittleLatLng paramUberLatLng)
    {
        this.mAccuracy = paramFloat1;
        this.mAltitude = paramDouble;
        this.mBearing = paramFloat2;
        this.mSpeed = paramFloat3;
        this.mTime = paramLong;
        this.mLittleLocationLatLng = paramUberLatLng;
    }

    protected LittleLocation(Parcel paramParcel)
    {
        this.mAccuracy = paramParcel.readFloat();
        this.mAltitude = paramParcel.readDouble();
        this.mBearing = paramParcel.readFloat();
        this.mSpeed = paramParcel.readFloat();
        this.mTime = paramParcel.readLong();
        this.mLittleLocationLatLng = new LittleLatLng(paramParcel);
    }

    public int describeContents()
    {
        return 0;
    }

    public boolean equals(Object paramObject)
    {
        if (this == paramObject);
        LittleLocation localUberLocation;
        do
        {

            if ((paramObject == null) || (getClass() != paramObject.getClass()))
                return false;
            localUberLocation = (LittleLocation)paramObject;
            if (Float.compare(localUberLocation.mAccuracy, this.mAccuracy) != 0)
                return false;
            if (Double.compare(localUberLocation.mAltitude, this.mAltitude) != 0)
                return false;
            if (Float.compare(localUberLocation.mBearing, this.mBearing) != 0)
                return false;
            if (Float.compare(localUberLocation.mSpeed, this.mSpeed) != 0)
                return false;
            if (this.mTime != localUberLocation.mTime)
                return false;
            if (this.mLittleLocationLatLng == null)
                break;

            return true;
        }
        while (this.mLittleLocationLatLng.equals(localUberLocation.mLittleLocationLatLng));
        while (true)
        {

            if (localUberLocation.mLittleLocationLatLng == null)
                return false;
        }
    }

    public float getAccuracy()
    {
        return this.mAccuracy;
    }

    public double getAltitude()
    {
        return this.mAltitude;
    }

    public float getBearing()
    {
        return this.mBearing;
    }

    public float getSpeed()
    {
        return this.mSpeed;
    }

    public long getTime()
    {
        return this.mTime;
    }

    public LittleLatLng getUberLatLng()
    {
        return this.mLittleLocationLatLng;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
        paramParcel.writeFloat(this.mAccuracy);
        paramParcel.writeDouble(this.mAltitude);
        paramParcel.writeFloat(this.mBearing);
        paramParcel.writeFloat(this.mSpeed);
        paramParcel.writeLong(this.mTime);
        this.mLittleLocationLatLng.writeToParcel(paramParcel, paramInt);
    }

    public static class Builder
    {
        private float accuracy;
        private double altitude;
        private float bearing;
        private float speed;
        private long time;
        private final LittleLatLng uberLatLng;

        public Builder(LittleLatLng paramUberLatLng)
        {
            this.uberLatLng = paramUberLatLng;
        }

        public Builder accuracy(float paramFloat)
        {
            this.accuracy = paramFloat;
            return this;
        }

        public Builder altitude(double paramDouble)
        {
            this.altitude = paramDouble;
            return this;
        }

        public Builder bearing(float paramFloat)
        {
            this.bearing = paramFloat;
            return this;
        }

        public LittleLocation build()
        {
            return new LittleLocation(this.accuracy, this.altitude, this.bearing, this.speed, this.time, this.uberLatLng);
        }

        public Builder speed(float paramFloat)
        {
            this.speed = paramFloat;
            return this;
        }

        public Builder time(long paramLong)
        {
            this.time = paramLong;
            return this;
        }
    }
}
