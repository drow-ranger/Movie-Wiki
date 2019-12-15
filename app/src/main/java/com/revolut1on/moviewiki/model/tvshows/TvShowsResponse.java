package com.revolut1on.moviewiki.model.tvshows;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TvShowsResponse implements Parcelable {

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<TvShowsItem> results;

	@SerializedName("total_results")
	private int totalResults;

	public void setPage(int page){
		this.page = page;
	}

	public int getPage(){
		return page;
	}

	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public void setResults(List<TvShowsItem> results){
		this.results = results;
	}

	public List<TvShowsItem> getResults(){
		return results;
	}

	public void setTotalResults(int totalResults){
		this.totalResults = totalResults;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.page);
		dest.writeTypedList(this.results);
		dest.writeInt(this.totalResults);
		dest.writeInt(this.totalPages);
	}

	public TvShowsResponse() {
	}

	protected TvShowsResponse(Parcel in) {
		this.page = in.readInt();
		this.results = in.createTypedArrayList(TvShowsItem.CREATOR);
		this.totalResults = in.readInt();
		this.totalPages = in.readInt();
	}

	public static final Parcelable.Creator<TvShowsResponse> CREATOR = new Parcelable.Creator<TvShowsResponse>() {
		@Override
		public TvShowsResponse createFromParcel(Parcel source) {
			return new TvShowsResponse(source);
		}

		@Override
		public TvShowsResponse[] newArray(int size) {
			return new TvShowsResponse[size];
		}
	};
}