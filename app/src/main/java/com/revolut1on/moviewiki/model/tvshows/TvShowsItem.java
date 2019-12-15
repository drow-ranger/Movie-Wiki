package com.revolut1on.moviewiki.model.tvshows;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TvShowsItem implements Parcelable {

	@SerializedName("first_air_date")
	private String firstAirDate;

	@SerializedName("overview")
	private String overview;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("genre_ids")
	private List<Integer> genreIds;

	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("origin_country")
	private List<String> originCountry;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("original_name")
	private String originalName;

	@SerializedName("popularity")
	private double popularity;

	@SerializedName("vote_average")
	private double voteAverage;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("vote_count")
	private int voteCount;

	public void setFirstAirDate(String firstAirDate){
		this.firstAirDate = firstAirDate;
	}

	public String getFirstAirDate(){
		return firstAirDate;
	}

	public void setOverview(String overview){
		this.overview = overview;
	}

	public String getOverview(){
		return overview;
	}

	public void setOriginalLanguage(String originalLanguage){
		this.originalLanguage = originalLanguage;
	}

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public void setGenreIds(List<Integer> genreIds){
		this.genreIds = genreIds;
	}

	public List<Integer> getGenreIds(){
		return genreIds;
	}

	public void setPosterPath(String posterPath){
		this.posterPath = posterPath;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public void setOriginCountry(List<String> originCountry){
		this.originCountry = originCountry;
	}

	public List<String> getOriginCountry(){
		return originCountry;
	}

	public void setBackdropPath(String backdropPath){
		this.backdropPath = backdropPath;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public void setOriginalName(String originalName){
		this.originalName = originalName;
	}

	public String getOriginalName(){
		return originalName;
	}

	public void setPopularity(double popularity){
		this.popularity = popularity;
	}

	public double getPopularity(){
		return popularity;
	}

	public void setVoteAverage(double voteAverage){
		this.voteAverage = voteAverage;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setVoteCount(int voteCount){
		this.voteCount = voteCount;
	}

	public int getVoteCount(){
		return voteCount;
	}

	public TvShowsItem(String posterPath, String overview, String firstAirDate, List<Integer> genreIds, Integer id,
					  String originalName, String originalLanguage, String name, String backdropPath, Double popularity,
					  Integer voteCount, Double voteAverage) {
		this.posterPath = posterPath;
		this.overview = overview;
		this.firstAirDate = firstAirDate;
		this.genreIds = genreIds;
		this.id = id;
		this.originalName = originalName;
		this.originalLanguage = originalLanguage;
		this.name = name;
		this.backdropPath = backdropPath;
		this.popularity = popularity;
		this.voteCount = voteCount;
		this.voteAverage = voteAverage;
	}

	public TvShowsItem() {

	}

	public static final Comparator<TvShowsItem> BY_NAME_ALPHABETICAL = new Comparator<TvShowsItem>() {
		@Override
		public int compare(TvShowsItem movie, TvShowsItem t1) {

			return movie.originalName.compareTo(t1.originalName);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.posterPath);
		dest.writeString(this.overview);
		dest.writeString(this.firstAirDate);
		dest.writeList(this.genreIds);
		dest.writeValue(this.id);
		dest.writeString(this.originalName);
		dest.writeString(this.originalLanguage);
		dest.writeString(this.name);
		dest.writeString(this.backdropPath);
		dest.writeValue(this.popularity);
		dest.writeValue(this.voteCount);
		dest.writeValue(this.voteAverage);
	}

	protected TvShowsItem(Parcel in) {
		this.posterPath = in.readString();
		this.overview = in.readString();
		this.firstAirDate = in.readString();
		this.genreIds = new ArrayList<Integer>();
		in.readList(this.genreIds, Integer.class.getClassLoader());
		this.id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.originalName = in.readString();
		this.originalLanguage = in.readString();
		this.name = in.readString();
		this.backdropPath = in.readString();
		this.popularity = (Double) in.readValue(Double.class.getClassLoader());
		this.voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
		this.voteAverage = (Double) in.readValue(Double.class.getClassLoader());
	}

	public static final Parcelable.Creator<TvShowsItem> CREATOR = new Parcelable.Creator<TvShowsItem>() {
		@Override
		public TvShowsItem createFromParcel(Parcel source) {
			return new TvShowsItem(source);
		}

		@Override
		public TvShowsItem[] newArray(int size) {
			return new TvShowsItem[size];
		}
	};
}