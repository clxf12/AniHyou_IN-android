package com.axiel7.anihyou.ui.mediadetails

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.apollographql.apollo3.api.Optional
import com.axiel7.anihyou.DeleteMediaListMutation
import com.axiel7.anihyou.data.model.duration
import com.axiel7.anihyou.data.model.isManga
import com.axiel7.anihyou.fragment.BasicMediaDetails
import com.axiel7.anihyou.fragment.BasicMediaListEntry
import com.axiel7.anihyou.type.MediaListStatus
import com.axiel7.anihyou.ui.base.BaseViewModel
import java.time.LocalDate

class EditMediaViewModel(
    private val mediaDetails: BasicMediaDetails,
    private var listEntry: BasicMediaListEntry?
) : BaseViewModel() {

    val isNewEntry by derivedStateOf { listEntry == null }

    var status by mutableStateOf(listEntry?.status)
    var progress by mutableStateOf(listEntry?.progress)
    var volumeProgress by mutableStateOf(listEntry?.progressVolumes)
    var score by mutableStateOf(0)
    var startDate by mutableStateOf<LocalDate?>(null)
    var endDate by mutableStateOf<LocalDate?>(null)
    var repeatCount by mutableStateOf(0)

    fun onChangeStatus(value: MediaListStatus) {
        status = value
        if (isNewEntry && value == MediaListStatus.CURRENT) {
            startDate = LocalDate.now()
        }
        else if (value == MediaListStatus.COMPLETED) {
            endDate = LocalDate.now()
            mediaDetails.duration()?.let { if (it > 0) progress = it }
            if (mediaDetails.isManga()) {
                mediaDetails.volumes?.let { if (it > 0) volumeProgress = it }
            }
        }
    }

    fun onChangeProgress(value: Int?) {
        if (canChangeProgressTo(value, mediaDetails.duration())) {
            progress = value
            if (status == MediaListStatus.PLANNING) {
                status = MediaListStatus.CURRENT
            }
        }
    }

    private fun canChangeProgressTo(value: Int?, limit: Int?) = when {
        value == null -> true //allow to set empty
        value < 0 -> false //progress must be positive
        value == 0 -> true //allow set to 0
        limit == null -> true //no limitations
        limit <= 0 -> true //no limitations
        value <= limit -> true //progress must be below total
        else -> false
    }

    fun onChangeVolumeProgress(value: Int?) {
        if (canChangeProgressTo(value, mediaDetails.volumes)) {
            volumeProgress = value
            if (status == MediaListStatus.PLANNING) {
                status = MediaListStatus.CURRENT
            }
        }
    }

    fun onChangeRepeatCount(value: Int?): Boolean {
        if (value != null && value >= 0) {
            repeatCount = value
            return true
        }
        return false
    }

    var openDatePicker by mutableStateOf(false)
    var selectedDateType by mutableStateOf(-1)

    var updateSuccess by mutableStateOf(false)

    suspend fun updateListEntry() {
        isLoading = true


        isLoading = false
    }

    var openDeleteDialog by mutableStateOf(false)

    suspend fun deleteListEntry() {
        if (listEntry == null) return
        isLoading = true
        val response = DeleteMediaListMutation(
            mediaListEntryId = Optional.present(listEntry!!.id)
        ).tryMutation()

        updateSuccess = response != null

        isLoading = false
    }
}