package com.amirhparhizgar.revision.service.human_readable_date

import android.content.Context
import androidx.core.os.ConfigurationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DateTranslatableStringsImpl @Inject constructor(@ApplicationContext val context: Context) :
    DateTranslatableStringsTestDouble() {
    override val locale = ConfigurationCompat.getLocales(context.resources.configuration).get(0);
    // TODO in this class we can localize strings
}