package com.qure.policy

import android.content.Context
import android.content.Intent
import com.qure.navigator.ProgramInformationNavigator
import com.qure.program_information.ProgramInformationActivity
import javax.inject.Inject

class ProgramInformationNavigatorImpl @Inject constructor(): ProgramInformationNavigator {
    override fun intent(context: Context): Intent {
        return Intent(context, ProgramInformationActivity::class.java)
    }
}