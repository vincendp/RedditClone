import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { ApiService } from "src/app/Core/Http/api.service";
import { User } from "src/app/Core/Model/user.model";
import { switchMap } from "rxjs/operators";
import { PostPreview } from "src/app/Core/Model/post-preview.model";
@Component({
  selector: "app-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
})
export class UserProfileComponent implements OnInit {
  queryUser: User;
  postPreviews: Array<PostPreview>;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.queryUser = {} as User;
    this.postPreviews = [] as Array<PostPreview>;

    let username = this.route.snapshot.params.username;

    this.apiService
      .get(`/users/${username}`, null)
      .pipe(
        switchMap((user) => {
          this.queryUser = user["result"] as User;

          return this.apiService.get(`/posts/users/${this.queryUser.id}`, null);
        })
      )
      .subscribe(
        (postPreviews) => {
          this.postPreviews = postPreviews["result"] as Array<PostPreview>;
        },
        (err) => {
          if (err.status == 404) {
            this.router.navigateByUrl("/error/not-found");
          }
        }
      );
  }
}
