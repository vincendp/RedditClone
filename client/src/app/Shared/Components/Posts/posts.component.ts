import { Component, OnInit, OnDestroy, Input } from "@angular/core";
import { environment } from "src/environments/environment";
import { PostType } from "src/app/Core/Model/post-type.enum";
import { PostPreview } from "src/app/Core/Model/post-preview.model";
import { ApiService } from "src/app/Core/Http/api.service";
import { UserService } from "src/app/Core/Services/user.service";
import { User } from "src/app/Core/Model/user.model";
import { takeUntil } from "rxjs/operators";
import { Subject } from "rxjs";

@Component({
  selector: "app-posts",
  templateUrl: "./posts.component.html",
  styleUrls: ["./posts.component.scss"],
})
export class PostsComponent implements OnInit, OnDestroy {
  PostType = PostType;
  apiUrl: string = environment.apiUrl;

  @Input() includeUser: boolean;
  @Input() includeSubreddit: boolean;
  @Input() postPreviews: Array<PostPreview>;

  destroy: Subject<Boolean> = new Subject();

  user: User;

  constructor(
    private apiService: ApiService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.userService.user.pipe(takeUntil(this.destroy)).subscribe((user) => {
      this.user = user as User;
    });
  }

  ngOnDestroy(): void {
    this.destroy.next(true);
    this.destroy.unsubscribe();
  }

  castVote(postPreview: PostPreview, vote: boolean) {
    if (postPreview.user_voted_for_post != 0) {
      if (
        (postPreview.user_voted_for_post == -1 && !vote) ||
        (postPreview.user_voted_for_post == 1 && vote)
      ) {
        this.deleteVote(postPreview, vote);
      } else {
        this.updateVote(postPreview, vote);
      }
    } else {
      this.createVote(postPreview, vote);
    }
  }

  createVote(postPreview: PostPreview, vote: boolean) {
    this.apiService
      .post(
        "/votes/posts",
        {
          post_id: postPreview.post_id,
          user_id: this.user.id,
          vote: vote,
        },
        {}
      )
      .subscribe(
        () => {
          postPreview.votes += vote ? 1 : -1;
          postPreview.user_voted_for_post = vote ? 1 : -1;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  updateVote(postPreview: PostPreview, vote: boolean) {
    this.apiService
      .put(
        "/votes/posts",
        {
          post_id: postPreview.post_id,
          user_id: this.user.id,
          vote: vote,
        },
        {}
      )
      .subscribe(
        () => {
          postPreview.votes += vote ? 2 : -2;
          postPreview.user_voted_for_post = vote ? 1 : -1;
        },
        (err) => {
          console.log(err);
        }
      );
  }

  deleteVote(postPreview: PostPreview, vote: boolean) {
    this.apiService
      .delete(
        "/votes/posts",
        {
          post_id: postPreview.post_id,
          user_id: this.user.id,
        },
        {}
      )
      .subscribe(
        () => {
          postPreview.votes += vote ? -1 : 1;
          postPreview.user_voted_for_post = 0;
        },
        (err) => {
          console.log(err);
        }
      );
  }
}
