export interface Comment {
  comment_id: string;
  comment: string;
  created_at: Date;
  user_id: string;
  username: string;
  votes: number;
  user_voted_for_comment: Boolean;
}
