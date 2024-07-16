import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import VideoPlayer from "../Video Components/VideoPlayer";
import VideoInfo from "./VideoInfo";
import CommentsSection from "../Comment Component/CommentsSection";
import VideoRecommendation from "../Video Components/VideoRecommendation";
import "./VideoDetail.css";
import { VideosContext } from "../VideosContext";
import { UserContext } from "../UserContext";
import { toggleVideoDislike, toggleVideoLike } from "../api/video"; // Import API functions
import { getMediaSource } from "../utils/mediaUtils";

function VideoDetail() {
  const { id } = useParams();
  const { videos, updateVideo, getVideoById } = useContext(VideosContext);
  const { user } = useContext(UserContext);
  const [video, setVideo] = useState(null);
  const [likes, setLikes] = useState(0);
  const [dislikes, setDislikes] = useState(0);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notFound, setNotFound] = useState(false);

  useEffect(() => {
    const fetchVideo = async () => {
      try {
        const fetchedVideo = await getVideoById(id);
        setVideo(fetchedVideo);
        setLikes(fetchedVideo.likes || 0);
        setDislikes(fetchedVideo.dislikes || 0);
        setComments(fetchedVideo.comments || []);
      } catch (error) {
        if (error.response && error.response.status === 404) {
          setNotFound(true); // Set not found state if video does not exist
        } else {
          console.error("Error loading video:", error);
        }
      } finally {
        setLoading(false);
      }
    };
    fetchVideo();
  }, [id, videos]);

  const handleLikeClick = async () => {
    if (!user) {
      alert("Please log in to like the video.");
      return;
    }

    try {
      const updatedVideo = await toggleVideoLike(video._id);
      setVideo(updatedVideo);
      setLikes(updatedVideo.likes);
      setDislikes(updatedVideo.dislikes);
      updateVideo(updatedVideo._id, updatedVideo);
    } catch (error) {
      console.error("Error toggling like on video:", error);
    }
  };

  const handleDislikeClick = async () => {
    if (!user) {
      alert("Please log in to dislike the video.");
      return;
    }

    try {
      const updatedVideo = await toggleVideoDislike(video._id);
      setVideo(updatedVideo);
      setLikes(updatedVideo.likes);
      setDislikes(updatedVideo.dislikes);
      updateVideo(updatedVideo._id, updatedVideo);
    } catch (error) {
      console.error("Error toggling dislike on video:", error);
    }
  };

  const handleAddComment = (comment) => {
    if (!user) {
      alert("Please log in to comment.");
      return;
    }

    const newComments = [...comments, comment];
    setComments(newComments);
    updateVideo(video._id, { comments: newComments });
  };

  const handleUpdateComments = (updatedComments) => {
    setComments(updatedComments);
    updateVideo(video._id, { comments: updatedComments });
  };

  if (loading) return <div>Loading...</div>;
  if (notFound) return <div>Video not found</div>;

  return (
    <div className="video-detail container mt-4">
      <div className="video-content">
        <VideoPlayer
          key={video._id}
          videoUrl={getMediaSource(video.url)}
          thumbnail={video.thumbnail}
        />
        <VideoInfo
          video={video}
          likes={likes}
          dislikes={dislikes}
          onLike={handleLikeClick}
          onDislike={handleDislikeClick}
        />
        <CommentsSection
          videoId={video._id}
          comments={comments}
          onAddComment={handleAddComment}
          onUpdateComments={handleUpdateComments}
        />
      </div>
      <div className="video-recommendations">
        {videos
          .filter((v) => v._id.toString() !== id)
          .map((video) => (
            <VideoRecommendation key={video._id} {...video} />
          ))}
      </div>
    </div>
  );
}

export default VideoDetail;
