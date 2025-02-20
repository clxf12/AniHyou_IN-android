package com.axiel7.anihyou.ui.characterdetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.apollographql.apollo3.api.Optional
import com.axiel7.anihyou.CharacterDetailsQuery
import com.axiel7.anihyou.CharacterMediaQuery
import com.axiel7.anihyou.ToggleFavouriteMutation
import com.axiel7.anihyou.type.MediaType
import com.axiel7.anihyou.ui.base.BaseViewModel

class CharacterDetailsViewModel : BaseViewModel() {

    var characterDetails by mutableStateOf<CharacterDetailsQuery.Character?>(null)
    var alternativeNames by mutableStateOf<String?>(null)
    var alternativeNamesSpoiler by mutableStateOf<String?>(null)

    suspend fun getCharacterDetails(characterId: Int) {
        isLoading = true
        val response = CharacterDetailsQuery(
            characterId = Optional.present(characterId)
        ).tryQuery()

        response?.data?.Character?.let {
            characterDetails = it
            alternativeNames = it.name?.alternative?.filterNotNull()?.joinToString()
            alternativeNamesSpoiler = it.name?.alternativeSpoiler?.filterNotNull()?.joinToString()
        }
        isLoading = false
    }

    suspend fun toggleFavorite() {
        characterDetails?.let { details ->
            val response = ToggleFavouriteMutation(
                characterId = Optional.present(details.id)
            ).tryMutation()

            if (response?.data != null) {
                characterDetails = details.copy(isFavourite = !details.isFavourite)
            }
        }
    }

    var page = 1
    var hasNextPage = true
    var characterMedia =  mutableStateListOf<CharacterMediaQuery.Edge>()

    suspend fun getCharacterMedia(characterId: Int) {
        isLoading = page == 1
        val response = CharacterMediaQuery(
            characterId = Optional.present(characterId),
            page = Optional.present(page),
            perPage = Optional.present(25)
        ).tryQuery()

        response?.data?.Character?.media?.edges?.filterNotNull()?.let { characterMedia.addAll(it) }
        page = response?.data?.Character?.media?.pageInfo?.currentPage?.plus(1) ?: page
        hasNextPage = response?.data?.Character?.media?.pageInfo?.hasNextPage ?: false
        isLoading = false
    }
}