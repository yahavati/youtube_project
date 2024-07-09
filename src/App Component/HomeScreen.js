import React, { useContext } from "react";
import VideoList from "../Video Components/VideoList";
import { VideosContext } from "../VideosContext";
import { SearchQueryContext } from "../SearchQueryContext";

const HomeScreen = () => {
  const { videos } = useContext(VideosContext);
  const { searchQuery } = useContext(SearchQueryContext);
  return <VideoList videos={videos} searchQuery={searchQuery} />;
};

export default HomeScreen;
