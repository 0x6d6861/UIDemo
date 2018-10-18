package utils.Location;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public final class LittleLatLng
        implements Parcelable
{
    public static final Parcelable.Creator<LittleLatLng> CREATOR = new Parcelable.Creator()
    {
        public LittleLatLng createFromParcel(Parcel paramAnonymousParcel)
        {
            return new LittleLatLng(paramAnonymousParcel);
        }

        public LittleLatLng[] newArray(int paramAnonymousInt)
        {
            return new LittleLatLng[paramAnonymousInt];
        }
    };
    private static final double EARTH_A = 6378245.0D;
    private static final double EARTH_EE = 0.006693421622965943D;
    public static final int PROJECTION_BD09 = 2;
    public static final int PROJECTION_GCJ02 = 1;
    public static final int PROJECTION_WGS84 = 0;
    private static final double X_PI = 52.359877559829883D;
    private final double mLatitude;
    private final double mLongitude;
    private final int mProjection;

    public LittleLatLng(double paramDouble1, double paramDouble2)
    {
        this(paramDouble1, paramDouble2, 0);
    }

    public LittleLatLng(double paramDouble1, double paramDouble2, int paramInt)
    {
        this.mLatitude = paramDouble1;
        this.mLongitude = paramDouble2;
        this.mProjection = paramInt;
    }

    public LittleLatLng(Parcel paramParcel)
    {
        this.mLatitude = paramParcel.readDouble();
        this.mLongitude = paramParcel.readDouble();
        this.mProjection = paramParcel.readInt();
    }

    private static LittleLatLng convertBD09ToGCJ02(LittleLatLng paramLittleLatLng)
    {
        double d1 = paramLittleLatLng.getLongitude() - 0.00665D;
        double d2 = paramLittleLatLng.getLatitude() - 0.006D;
        double d3 = Math.sqrt(d1 * d1 + d2 * d2) - 2.E-005D * Math.sin(52.359877559829883D * d2);
        double d4 = Math.atan2(d2, d1) - 3.E-006D * Math.cos(52.359877559829883D * d1);
        double d5 = d3 * Math.cos(d4);
        return new LittleLatLng(d3 * Math.sin(d4), d5, 1);
    }

    private static LittleLatLng convertGCJ02ToBD09(LittleLatLng paramLittleLatLng)
    {
        double d1 = paramLittleLatLng.getLongitude();
        double d2 = paramLittleLatLng.getLatitude();
        double d3 = Math.sqrt(d1 * d1 + d2 * d2) + 2.E-005D * Math.sin(52.359877559829883D * d2);
        double d4 = Math.atan2(d2, d1) + 3.E-006D * Math.cos(52.359877559829883D * d1);
        double d5 = 0.0065D + d3 * Math.cos(d4);
        return new LittleLatLng(0.006D + d3 * Math.sin(d4), d5, 2);
    }

    private static LittleLatLng convertGCJ02ToWGS84(LittleLatLng paramLittleLatLng)
    {
        LittleLatLng localLittleLatLng = convertWGS84ToGCJ02(paramLittleLatLng);
        double d1 = localLittleLatLng.getLatitude() - paramLittleLatLng.getLatitude();
        double d2 = localLittleLatLng.getLongitude() - paramLittleLatLng.getLongitude();
        return new LittleLatLng(paramLittleLatLng.getLatitude() - d1, paramLittleLatLng.getLongitude() - d2, 0);
    }

    private static LittleLatLng convertWGS84ToGCJ02(LittleLatLng paramLittleLatLng)
    {
        double d1 = Math.toRadians(paramLittleLatLng.getLatitude());
        double d2 = 1.0D - 0.006693421622965943D * Math.sin(d1) * Math.sin(d1);
        double d3 = Math.sqrt(d2);
        double d4 = 180.0D * latitudeOffset(paramLittleLatLng.getLongitude() - 105.0D, paramLittleLatLng.getLatitude() - 35.0D) / (3.141592653589793D * (6335552.7170004258D / (d2 * d3)));
        double d5 = 180.0D * longitudeOffset(paramLittleLatLng.getLongitude() - 105.0D, paramLittleLatLng.getLatitude() - 35.0D) / (3.141592653589793D * (6378245.0D / d3 * Math.cos(d1)));
        return new LittleLatLng(d4 + paramLittleLatLng.getLatitude(), d5 + paramLittleLatLng.getLongitude(), 1);
    }

    public static double distanceInMeters(LittleLatLng paramLittleLatLng1, LittleLatLng paramLittleLatLng2)
    {
        LittleLatLng localLittleLatLng1 = paramLittleLatLng1.convertToWGS84();
        LittleLatLng localLittleLatLng2 = paramLittleLatLng2.convertToWGS84();
        float[] arrayOfFloat = new float[1];
        Location.distanceBetween(localLittleLatLng1.getLatitude(), localLittleLatLng1.getLongitude(), localLittleLatLng2.getLatitude(), localLittleLatLng2.getLongitude(), arrayOfFloat);
        return arrayOfFloat[0];
    }

    private static double latitudeOffset(double paramDouble1, double paramDouble2)
    {
        return -100.0D + 2.0D * paramDouble1 + 3.0D * paramDouble2 + paramDouble2 * (0.2D * paramDouble2) + paramDouble2 * (0.1D * paramDouble1) + 0.2D * Math.sqrt(Math.abs(paramDouble1)) + 2.0D * (20.0D * Math.sin(3.141592653589793D * (6.0D * paramDouble1)) + 20.0D * Math.sin(3.141592653589793D * (2.0D * paramDouble1))) / 3.0D + 2.0D * (20.0D * Math.sin(3.141592653589793D * paramDouble2) + 40.0D * Math.sin(3.141592653589793D * (paramDouble2 / 3.0D))) / 3.0D + 2.0D * (160.0D * Math.sin(3.141592653589793D * (paramDouble2 / 12.0D)) + 320.0D * Math.sin(3.141592653589793D * paramDouble2 / 30.0D)) / 3.0D;
    }

    private static double longitudeOffset(double paramDouble1, double paramDouble2)
    {
        return 300.0D + paramDouble1 + 2.0D * paramDouble2 + paramDouble1 * (0.1D * paramDouble1) + paramDouble2 * (0.1D * paramDouble1) + 0.1D * Math.sqrt(Math.abs(paramDouble1)) + 2.0D * (20.0D * Math.sin(3.141592653589793D * (6.0D * paramDouble1)) + 20.0D * Math.sin(3.141592653589793D * (2.0D * paramDouble1))) / 3.0D + 2.0D * (20.0D * Math.sin(3.141592653589793D * paramDouble1) + 40.0D * Math.sin(3.141592653589793D * (paramDouble1 / 3.0D))) / 3.0D + 2.0D * (150.0D * Math.sin(3.141592653589793D * (paramDouble1 / 12.0D)) + 300.0D * Math.sin(3.141592653589793D * (paramDouble1 / 30.0D))) / 3.0D;
    }

    public LittleLatLng convertToBD09()
    {
        switch (this.mProjection)
        {
            default:
                throw new RuntimeException("Conversion doesn't exist for projections.");
            case 2:
                return new LittleLatLng(this.mLatitude, this.mLongitude, this.mProjection);
            case 0:
                return convertGCJ02ToBD09(convertWGS84ToGCJ02(this));
            case 1:
        }
        return convertGCJ02ToBD09(this);
    }

    public LittleLatLng convertToGCJ02()
    {
        switch (this.mProjection)
        {
            default:
                throw new RuntimeException("Conversion doesn't exist for projections.");
            case 1:
                return new LittleLatLng(this.mLatitude, this.mLongitude, this.mProjection);
            case 0:
                return convertWGS84ToGCJ02(this);
            case 2:
        }
        return convertBD09ToGCJ02(this);
    }

    public LittleLatLng convertToWGS84()
    {
        switch (this.mProjection)
        {
            default:
                throw new RuntimeException("Conversion doesn't exist for projections.");
            case 0:
                return new LittleLatLng(this.mLatitude, this.mLongitude, this.mProjection);
            case 1:
                return convertGCJ02ToWGS84(this);
            case 2:
        }
        return convertGCJ02ToWGS84(convertBD09ToGCJ02(this));
    }

    public int describeContents()
    {
        return 0;
    }

    public boolean equals(Object paramObject)
    {
        if (this == paramObject);
        LittleLatLng localLittleLatLng;
        do
        {
            //return true;
            if ((paramObject == null) || (getClass() != paramObject.getClass())) {
                return false;
            }
            localLittleLatLng = (LittleLatLng)paramObject;
            if (Double.compare(localLittleLatLng.getLatitude(), this.mLatitude) != 0)
                return false;
            if (Double.compare(localLittleLatLng.getLongitude(), this.mLongitude) != 0)
                return false;

            //return true;
        }
        while (this.mProjection == localLittleLatLng.getProjection());
        return false;
    }

    public double getLatitude()
    {
        return this.mLatitude;
    }

    public double getLongitude()
    {
        return this.mLongitude;
    }

    public int getProjection()
    {
        return this.mProjection;
    }

    public int hashCode()
    {
        long l1 = Double.doubleToLongBits(this.mLatitude);
        int i = (int)(l1 ^ l1 >>> 32);
        long l2 = Double.doubleToLongBits(this.mLongitude);
        return 31 * (i * 31 + (int)(l2 ^ l2 >>> 32)) + this.mProjection;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
        paramParcel.writeDouble(this.mLatitude);
        paramParcel.writeDouble(this.mLongitude);
        paramParcel.writeInt(this.mProjection);
    }
}