package com.example.loomi.data.response

import com.google.gson.annotations.SerializedName

data class ResponseArticle(

	@field:SerializedName("ResponseArticle")
	val responseArticle: List<ResponseArticleItem>
)

data class User(

	@field:SerializedName("profile_image")
	val profileImage: String,

	@field:SerializedName("website_url")
	val websiteUrl: Any,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("twitter_username")
	val twitterUsername: Any?,

	@field:SerializedName("github_username")
	val githubUsername: String?,

	@field:SerializedName("profile_image_90")
	val profileImage90: String,

	@field:SerializedName("username")
	val username: String
)

data class ResponseArticleItem(

	@field:SerializedName("readable_publish_date")
	val readablePublishDate: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("collection_id")
	val collectionId: Any,

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("tag_list")
	val tagList: List<Any>,

	@field:SerializedName("type_of")
	val typeOf: String,

	@field:SerializedName("edited_at")
	val editedAt: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("cover_image")
	val coverImage: Any,

	@field:SerializedName("published_at")
	val publishedAt: String,

	@field:SerializedName("published_timestamp")
	val publishedTimestamp: String,

	@field:SerializedName("slug")
	val slug: String,

	@field:SerializedName("canonical_url")
	val canonicalUrl: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("tags")
	val tags: String,

	@field:SerializedName("social_image")
	val socialImage: String,

	@field:SerializedName("positive_reactions_count")
	val positiveReactionsCount: Int,

	@field:SerializedName("comments_count")
	val commentsCount: Int,

	@field:SerializedName("public_reactions_count")
	val publicReactionsCount: Int,

	@field:SerializedName("last_comment_at")
	val lastCommentAt: String,

	@field:SerializedName("reading_time_minutes")
	val readingTimeMinutes: Int,

	@field:SerializedName("crossposted_at")
	val crosspostedAt: Any,

	@field:SerializedName("user")
	val user: User
)
