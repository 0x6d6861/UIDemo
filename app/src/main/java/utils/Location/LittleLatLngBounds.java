package utils.Location;


import android.os.Parcel;
import android.os.Parcelable;

public final class LittleLatLngBounds implements Parcelable
{
    public static final Parcelable.Creator<LittleLatLngBounds> CREATOR = new Parcelable.Creator()
    {
        public LittleLatLngBounds createFromParcel(Parcel paramAnonymousParcel)
        {
            return new LittleLatLngBounds(paramAnonymousParcel);
        }

        public LittleLatLngBounds[] newArray(int paramAnonymousInt)
        {
            return new LittleLatLngBounds[paramAnonymousInt];
        }
    };
    private final LittleLatLng mNortheast;
    private final LittleLatLng mSouthwest;

    protected LittleLatLngBounds(Parcel paramParcel)
    {
        this.mSouthwest = new LittleLatLng(paramParcel);
        this.mNortheast = new LittleLatLng(paramParcel);
    }

    public LittleLatLngBounds(LittleLatLng paramUberLatLng1, LittleLatLng paramUberLatLng2)
    {
        this.mSouthwest = paramUberLatLng1;
        this.mNortheast = paramUberLatLng2;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    private static double distanceFromNortheastLatitude(double paramDouble1, double paramDouble2)
    {
        return (360.0D + (paramDouble2 - paramDouble1)) % 360.0D;
    }

    private static double distanceFromSouthwestLatitude(double paramDouble1, double paramDouble2)
    {
        return (360.0D + (paramDouble1 - paramDouble2)) % 360.0D;
    }

    private boolean isInBetweenLatitudes(double paramDouble)
    {
        return (this.mSouthwest.getLatitude() <= paramDouble) && (paramDouble <= this.mNortheast.getLatitude());
    }

    private boolean isInBetweenLongitudes(double paramDouble)
    {
        if (this.mSouthwest.getLongitude() <= this.mNortheast.getLongitude())
            return (this.mSouthwest.getLongitude() <= paramDouble) && (paramDouble <= this.mNortheast.getLongitude());
        boolean bool1;
        if (this.mSouthwest.getLongitude() > paramDouble)
        {
            boolean bool2 = paramDouble < this.mNortheast.getLongitude();
            bool1 = false;
            if (bool2);
        }
        else
        {
            bool1 = true;
        }
        return bool1;
    }

    public boolean contains(LittleLatLng paramUberLatLng)
    {
        return (isInBetweenLatitudes(paramUberLatLng.getLatitude())) && (isInBetweenLongitudes(paramUberLatLng.getLongitude()));
    }

    public int describeContents()
    {
        return 0;
    }

    public boolean equals(Object paramObject)
    {
        if (this == paramObject);
        LittleLatLngBounds localLittleLatLngBounds;
        do
        {
            //return true;
            if ((paramObject == null) || (getClass() != paramObject.getClass()))
                return false;
            localLittleLatLngBounds = (LittleLatLngBounds)paramObject;
            if (this.mNortheast != null)
            {
                if (this.mNortheast.equals(localLittleLatLngBounds.getNortheast()));
            }
            else
                while (localLittleLatLngBounds.getNortheast() != null)
                    return false;
            if (this.mSouthwest == null)
                break;
        }
        while (this.mSouthwest.equals(localLittleLatLngBounds.getSouthwest()));
        while (true)
        {
            if (localLittleLatLngBounds.getSouthwest() == null)
                break;

            return false;
        }
        return false;
    }

    public LittleLatLng getCenter()
    {
        double d1 = (this.mSouthwest.getLatitude() + this.mNortheast.getLatitude()) / 2.0D;
        double d2 = this.mNortheast.getLongitude();
        double d3 = this.mSouthwest.getLongitude();
        if (d3 <= d2);
        for (double d4 = (d2 + d3) / 2.0D; ; d4 = (d3 + (360.0D + d2)) / 2.0D)
            return new LittleLatLng(d1, d4);
    }

    public LittleLatLng getNortheast()
    {
        return this.mNortheast;
    }

    public LittleLatLng getSouthwest()
    {
        return this.mSouthwest;
    }

    public int hashCode()
    {
        if (this.mSouthwest != null);
        for (int i = this.mSouthwest.hashCode(); ; i = 0)
        {
            int j = i * 31;
            LittleLatLng localUberLatLng = this.mNortheast;
            int k = 0;
            if (localUberLatLng != null)
                k = this.mNortheast.hashCode();
            return j + k;
        }
    }

    public LittleLatLngBounds including(LittleLatLng paramUberLatLng)
    {
        double d1 = Math.min(this.mSouthwest.getLatitude(), paramUberLatLng.getLatitude());
        double d2 = Math.max(this.mNortheast.getLatitude(), paramUberLatLng.getLatitude());
        double d3 = this.mNortheast.getLongitude();
        double d4 = this.mSouthwest.getLongitude();
        double d5 = paramUberLatLng.getLongitude();
        if (!isInBetweenLongitudes(d5))
        {
            if (distanceFromSouthwestLatitude(d4, d5) >= distanceFromNortheastLatitude(d3, d5)) {
                //return; //label115;
                d4 = d5;
            }
        }
        while (true)
        {
            return new LittleLatLngBounds(new LittleLatLng(d1, d4), new LittleLatLng(d2, d3));
            //label115: d3 = d5;
        }
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
        this.mSouthwest.writeToParcel(paramParcel, paramInt);
        this.mNortheast.writeToParcel(paramParcel, paramInt);
    }

    public static final class Builder
    {
        private double mNortheastLatitude = (-1.0D / 0.0D);
        private double mNortheastLongitude = (0.0D / 0.0D);
        private double mSouthwestLatitude = (1.0D / 0.0D);
        private double mSouthwestLongitude = (0.0D / 0.0D);

        private boolean isInBetweenLongitudes(double paramDouble)
        {
            if (this.mSouthwestLongitude <= this.mNortheastLongitude)
                return (this.mSouthwestLongitude <= paramDouble) && (paramDouble <= this.mNortheastLongitude);
            boolean bool1;
            if (this.mSouthwestLongitude > paramDouble)
            {
                boolean bool2 = paramDouble < this.mNortheastLongitude;
                bool1 = false;
                if (bool2);
            }
            else
            {
                bool1 = true;
            }
            return bool1;
        }

        public LittleLatLngBounds build()
        {
            return new LittleLatLngBounds(new LittleLatLng(this.mSouthwestLatitude, this.mSouthwestLongitude), new LittleLatLng(this.mNortheastLatitude, this.mNortheastLongitude));
        }

        public Builder include(LittleLatLng paramUberLatLng)
        {
            this.mSouthwestLatitude = Math.min(this.mSouthwestLatitude, paramUberLatLng.getLatitude());
            this.mNortheastLatitude = Math.max(this.mNortheastLatitude, paramUberLatLng.getLatitude());
            double d = paramUberLatLng.getLongitude();
            if (Double.isNaN(this.mSouthwestLongitude))
            {
                this.mSouthwestLongitude = d;
                this.mNortheastLongitude = d;
            }
            while (isInBetweenLongitudes(d))
                return this;
            if (LittleLatLngBounds.distanceFromSouthwestLatitude(this.mSouthwestLongitude, d) < LittleLatLngBounds.distanceFromNortheastLatitude(this.mNortheastLongitude, d))
            {
                this.mSouthwestLongitude = d;
                return this;
            }
            this.mNortheastLongitude = d;
            return this;
        }
    }
}
