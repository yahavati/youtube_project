import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import VideoPlayer from "../Video Components/VideoPlayer";
import VideoInfo from "./VideoInfo";
import CommentsSection from "../Comment Component/CommentsSection";
import VideoRecommendation from "../Video Components/VideoRecommendation";
import "./VideoDetail.css";
import { VideosContext } from "../VideosContext";
import { UserContext } from "../UserContext";

function VideoDetail() {
  const { id } = useParams();
  const { videos, updateVideo } = useContext(VideosContext);
  const { authenticatedUser } = useContext(UserContext);
  const [video, setVideo] = useState(null);
  const [likes, setLikes] = useState(0);
  const [dislikes, setDislikes] = useState(0);
  const [comments, setComments] = useState([]);

  useEffect(() => {
    const selectedVideo = videos.find((video) => video.id.toString() === id);
    if (selectedVideo) {
      setVideo(selectedVideo);
      setLikes(selectedVideo.likes || 0);
      setDislikes(selectedVideo.dislikes || 0);
      setComments(selectedVideo.comments || []);
    }
  }, [id, videos]);

  if (!video) {
    return <div>Video not found</div>;
  }

  const handleLikeClick = () => {
    if (!authenticatedUser) {
      alert("Please log in to like the video.");
      return;
    }

    const userAlreadyLiked =
      video.likedBy && video.likedBy.includes(authenticatedUser.username);
    const userAlreadyDisliked =
      video.dislikedBy && video.dislikedBy.includes(authenticatedUser.username);

    let newLikes = likes;
    let newDislikes = dislikes;
    let updatedLikedBy = video.likedBy ? [...video.likedBy] : [];
    let updatedDislikedBy = video.dislikedBy ? [...video.dislikedBy] : [];

    if (userAlreadyLiked) {
      newLikes -= 1;
      updatedLikedBy = updatedLikedBy.filter(
        (user) => user !== authenticatedUser.username
      );
    } else {
      newLikes += 1;
      updatedLikedBy.push(authenticatedUser.username);
      if (userAlreadyDisliked) {
        newDislikes -= 1;
        updatedDislikedBy = updatedDislikedBy.filter(
          (user) => user !== authenticatedUser.username
        );
      }
    }

    setLikes(newLikes);
    setDislikes(newDislikes);
    updateVideo(video.id, {
      likes: newLikes,
      dislikes: newDislikes,
      likedBy: updatedLikedBy,
      dislikedBy: updatedDislikedBy,
    });
  };

  const handleDislikeClick = () => {
    if (!authenticatedUser) {
      alert("Please log in to dislike the video.");
      return;
    }

    const userAlreadyLiked =
      video.likedBy && video.likedBy.includes(authenticatedUser.username);
    const userAlreadyDisliked =
      video.dislikedBy && video.dislikedBy.includes(authenticatedUser.username);

    let newLikes = likes;
    let newDislikes = dislikes;
    let updatedLikedBy = video.likedBy ? [...video.likedBy] : [];
    let updatedDislikedBy = video.dislikedBy ? [...video.dislikedBy] : [];

    if (userAlreadyDisliked) {
      newDislikes -= 1;
      updatedDislikedBy = updatedDislikedBy.filter(
        (user) => user !== authenticatedUser.username
      );
    } else {
      newDislikes += 1;
      updatedDislikedBy.push(authenticatedUser.username);
      if (userAlreadyLiked) {
        newLikes -= 1;
        updatedLikedBy = updatedLikedBy.filter(
          (user) => user !== authenticatedUser.username
        );
      }
    }

    setLikes(newLikes);
    setDislikes(newDislikes);
    updateVideo(video.id, {
      likes: newLikes,
      dislikes: newDislikes,
      likedBy: updatedLikedBy,
      dislikedBy: updatedDislikedBy,
    });
  };

  const handleAddComment = (comment) => {
    if (!authenticatedUser) {
      alert("Please log in to comment.");
      return;
    }

    const newComments = [...comments, comment];
    setComments(newComments);
    updateVideo(video.id, { comments: newComments });
  };

  const handleUpdateComments = (updatedComments) => {
    setComments(updatedComments);
    updateVideo(video.id, { comments: updatedComments });
  };

  return (
    <div className="video-detail container mt-4">
      <div className="video-content">
        <VideoPlayer key={video.id} videoUrl={video.videoUrl} />
        <VideoInfo
          video={video}
          likes={likes}
          dislikes={dislikes}
          onLike={handleLikeClick}
          onDislike={handleDislikeClick}
        />
        <CommentsSection
          videoId={video.id}
          comments={comments}
          onAddComment={handleAddComment}
          onUpdateComments={handleUpdateComments}
        />
      </div>
      <div className="video-recommendations">
        {videos
          .filter((v) => v.id.toString() !== id)
          .map((video) => (
            <VideoRecommendation key={video.id} {...video} />
          ))}
      </div>
    </div>
  );
}

export default VideoDetail;
