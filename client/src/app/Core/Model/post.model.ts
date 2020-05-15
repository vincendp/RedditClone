import { PostType } from "./post-type.enum";

export interface Post {
  post_id: string;
  post_type_id: PostType;
  title: string;
  description: string;
  image_path: string;
  link: string;
  created_at: Date;
  subreddit_id: string;
  subreddit: string;
  user_id: string;
  username: string;
  user_voted_for_post: Boolean;
  votes: number;
}
