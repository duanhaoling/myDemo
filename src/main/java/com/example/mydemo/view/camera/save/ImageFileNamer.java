
package com.example.mydemo.view.camera.save;

import java.text.SimpleDateFormat;
import java.util.Date;

class ImageFileNamer {
    private SimpleDateFormat mFormat;

    // The date (in milliseconds) used to generate the last name.
    private long mLastDate;

    // Number of names generated for the same second.
    private int mSameSecondCount;

    public ImageFileNamer(String format) {
        mFormat = new SimpleDateFormat(format);
    }

    public String generateName(long dateTaken) {
        Date date = new Date(dateTaken);
        String result = mFormat.format(date);

        // If the last name was generated for the same second,
        // we append _1, _2, etc to the name.
        if (dateTaken / 1000 == mLastDate / 1000) {
            mSameSecondCount++;
            result += "_" + mSameSecondCount;
        } else {
            mLastDate = dateTaken;
            mSameSecondCount = 0;
        }

        return result;
    }
}
