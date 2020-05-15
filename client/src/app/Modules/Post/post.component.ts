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
import { Subscription, throwError } from "rxjs";
import { UserService } from "src/app/Core/Services/user.service";
import { switchMap, catchError } from "rxjs/operators";

@Component({
  selector: "app-post",
  templateUrl: "./post.component.html",
  styleUrls: ["./post.component.scss"],
})
export class PostComponent implements OnInit, OnDestroy {
  apiUrl: string = environment.apiUrl;

  commentForm: FormControl;
  userSubscription: Subscription;

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

    this.userService.user.subscribe((user) => {
      this.user = user as User;
    });

    this.apiService
      .get(`/subreddits/${subredditName}`, null)
      .pipe(
        switchMap((subreddit) => {
          this.subreddit = subreddit["result"] as Subreddit;

          return this.apiService.get(`/posts/${postId}`, null);
        }),
        catchError((error) => {
          this.router.navigateByUrl("/error/not-found");
          return throwError(error);
        })
      )
      .subscribe(
        (post) => {
          this.post = post["result"] as Post;
          console.log(this.post);

          if (this.subreddit.id != this.post.subreddit_id) {
            this.router.navigateByUrl("/error/not-found");
          }
        },
        (err) => {
          console.log(err);
          this.router.navigateByUrl("/error/not-found");
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

  ngOnDestroy(): void {}

  submitComment(): void {
    this.apiService
      .post(
        "/comments",
        {
          comment: "hi",
          user_id: "b37cc7f0-1f93-4c22-98dd-72c5d26bd342",
          post_id: "af8c0603-af45-45ed-bc78-eebd88969c5f",
        },
        {}
      )
      .subscribe(
        (data) => {
          console.log(data);
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
