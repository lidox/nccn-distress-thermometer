package com.artursworld.nccn.controller.util;


import android.content.Intent;
import android.net.Uri;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.config.App;

import java.io.File;
import java.util.Date;

public class Files {

    /**
     * Share file via chooser
     *
     * @param file the file to share
     */
    public static void share(File file) {
        Uri u1 = null;
        u1 = Uri.fromFile(file);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);

        // get subject
        StringBuilder builder = new StringBuilder();
        builder.append(Strings.getStringByRId(R.string.app_name));
        builder.append(" ");
        builder.append(Strings.getStringByRId(R.string.exported_at));
        builder.append(" ");
        builder.append(Dates.getGermanDateByDate(new Date()));
        String subject = builder.toString();


        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        sendIntent.setType("text/html");
        App.getAppContext().startActivity(Intent.createChooser(sendIntent, Strings.getStringByRId(R.string.share)));
    }
}
