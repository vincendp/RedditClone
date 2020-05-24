import { Component, OnInit, OnDestroy } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { environment } from "src/environments/environment";
import { User } from "src/app/Core/Model/user.model";
import { Subreddit } from "src/app/Core/Model/subreddit.model";
import { Post } from "src/app/Core/Model/post.model";
import { Comment } from "src/app/Core/Model/comment.model";
import { PostType } from "src/app/Core/Model/post-type.enum";
import { ApiService } from "src/app/Core/Http/api.service";
import { FormControl, Validators } from "@angular/forms";
import { throwError, Subject } from "rxjs";
import { UserService } from "src/app/Core/Services/user.service";
import { switchMap, catchError, takeUntil } from "rxjs/operators";

@Component({
  selector: "app-post",
  templateUrl: "./post.component.html",
  styleUrls: ["./post.component.scss"],
})
export class PostComponent implements OnInit, OnDestroy {
  apiUrl: string = environment.apiUrl;

  commentForm: FormControl;
  destroy: Subject<Boolean> = new Subject();

  PostType = PostType;

  user: User;
  subreddit: Subreddit;
  post: Post;
  comments: Array<Comment>;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    let subredditName = this.route.snapshot.params.subreddit;
    let postId = this.route.snapshot.params.postId;

    this.user = {} as User;
    this.subreddit = {} as Subreddit;
    this.post = {} as Post;
    this.comments = [] as Array<Comment>;

    this.userService.user.pipe(takeUntil(this.destroy)).subscribe((user) => {
      this.user = user as User;
    });

    this.apiService
      .get(`/subreddits/${subredditName}`, null)
      .pipe(
        switchMap((subreddit) => {
          this.subreddit = subreddit["result"] as Subreddit;

          return this.apiService.get(`/posts/${postId}`, null);
        })
      )
      .subscribe(
        (post) => {
          this.post = post["result"] as Post;

          if (this.subreddit.id != this.post.subreddit_id) {
            this.router.navigateByUrl("/error/not-found");
          }
        },
        (err) => {
          if (err.status == 404) this.router.navigateByUrl("/error/not-found");
        }
      );

    this.apiService.get(`/comments/posts/${postId}`, null).subscribe(
      (data) => {
        this.comments = data["result"] as Array<Comment>;
      },
      (err) => {
        console.log(err);
      }
    );

    this.commentForm = new FormControl("");
    this.commentForm.setValidators([Validators.required]);
  }

  ngOnDestroy(): void {
    this.destroy.next(true);
    this.destroy.unsubscribe();
  }

  submitComment(): void {
    if (
      this.commentForm.value &&
      this.commentForm.value.length > 0 &&
      this.user &&
      this.user.id &&
      this.post &&
      this.post.post_id
    ) {
      this.apiService
        .post(
          "/comments",
          {
            comment: this.commentForm.value,
            user_id: this.user.id,
            post_id: this.post.post_id,
          },
          {}
        )
        .subscribe(
          (data) => {
            console.log(data);
            window.location.reload();
          },

          (err) => {
            console.log(err);
          }
        );
    }
  }

  castVoteForPost(vote: boolean) {
    if (this.post.user_voted_for_post != 0) {
      if (
        (this.post.user_voted_for_post == -1 && !vote) ||
        (this.post.user_voted_for_post == 1 && vote)
      ) {
        this.deleteVoteForPost(vote);
      } else {
        this.updateVoteForPost(vote);
      }
    } else {
      this.createVoteForPost(vote);
    }
  }

  createVoteForPost(vote: boolean) {
    this.apiService
      .post(
        "/votes/posts",
        {
          post_id: this.post.post_id,
          user_id: this.user.id,
          vote: vote,
        },
        {}
      )
      .subscribe(
        () => {
          this.post.votes += vote ? 1 : -1;
          this.post.user_voted_for_post = vote ? 1 : -1;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  updateVoteForPost(vote: boolean) {
    this.apiService
      .put(
        "/votes/posts",
        {
          post_id: this.post.post_id,
          user_id: this.user.id,
          vote: vote,
        },
        {}
      )
      .subscribe(
        () => {
          this.post.votes += vote ? 2 : -2;
          this.post.user_voted_for_post = vote ? 1 : -1;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  deleteVoteForPost(vote: boolean) {
    this.apiService
      .delete(
        "/votes/posts",
        {
          post_id: this.post.post_id,
          user_id: this.user.id,
        },
        {}
      )
      .subscribe(
        () => {
          this.post.votes += vote ? -1 : 1;
          this.post.user_voted_for_post = 0;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  castVoteForComment(comment: Comment, vote: boolean) {
    if (comment.user_voted_for_comment != 0) {
      if (
        (comment.user_voted_for_comment == -1 && !vote) ||
        (comment.user_voted_for_comment == 1 && vote)
      ) {
        this.deleteVoteForComment(comment, vote);
      } else {
        this.updateVoteForComment(comment, vote);
      }
    } else {
      this.createVoteForComment(comment, vote);
    }
  }

  createVoteForComment(comment: Comment, vote: boolean) {
    this.apiService
      .post(
        "/votes/comments",
        {
          comment_id: comment.comment_id,
          user_id: this.user.id,
          vote: vote,
        },
        {}
      )
      .subscribe(
        () => {
          comment.votes += vote ? 1 : -1;
          comment.user_voted_for_comment = vote ? 1 : -1;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  updateVoteForComment(comment: Comment, vote: boolean) {
    this.apiService
      .put(
        "/votes/comments",
        {
          comment_id: comment.comment_id,
          user_id: this.user.id,
          vote: vote,
        },
        {}
      )
      .subscribe(
        () => {
          comment.votes += vote ? 2 : -2;
          comment.user_voted_for_comment = vote ? 1 : -1;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  deleteVoteForComment(comment: Comment, vote: boolean) {
    this.apiService
      .delete(
        "/votes/comments",
        {
          comment_id: comment.comment_id,
          user_id: this.user.id,
        },
        {}
      )
      .subscribe(
        () => {
          comment.votes += vote ? -1 : 1;
          comment.user_voted_for_comment = 0;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  shortenURL(url: string) {
    let shortenedUrl = url;
    if (url && url.length >= 20) {
      shortenedUrl = shortenedUrl.substring(0, 20) + "...";
    }

    return shortenedUrl;
  }
}
