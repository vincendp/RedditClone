export interface PostPreview {
  post_id: string;
  post_type_id: number;
  title: string;
  image_path: string;
  link: string;
  created_at: Date;
  subreddit_id: string;
  subreddit: string;
  user_id: string;
  username: string;
  user_voted_for_post: number;
  votes: number;
  comments: number;
}
