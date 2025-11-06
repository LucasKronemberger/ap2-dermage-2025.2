package com.example.appdermageofc

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// o que enviamos

@Parcelize
data class QuizQuestion(
    @SerializedName("question") val question: String,
    @SerializedName("answer") val answer: String
) : Parcelable

data class SkinProfileRequest(
    @SerializedName("questions") val questions: List<QuizQuestion>,
    @SerializedName("others") val others: List<Map<String, String>>
)

// o que recebemos

@Parcelize
data class AnalysisResponse(
    @SerializedName("scores") val scores: List<SkinScore>,
    @SerializedName("concerns") val concerns: String,
    @SerializedName("skin_type") val skinType: String, // "mista", "seca", etc.
    @SerializedName("routine") val routine: SkinCareRoutine
) : Parcelable

@Parcelize
data class SkinScore(
    @SerializedName("score_tag") val scoreTag: String,
    @SerializedName("score_number") val scoreNumber: Float
) : Parcelable

@Parcelize
data class SkinCareRoutine(
    @SerializedName("morning") val morning: List<SkinCareProduct>,
    @SerializedName("night") val night: List<SkinCareProduct>
) : Parcelable

@Parcelize
data class SkinCareProduct(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Float,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("link") val link: String
) : Parcelable