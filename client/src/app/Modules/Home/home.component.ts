import { Component, OnInit, OnDestroy } from "@angular/core";
import { UserService } from "src/app/Core/Services/user.service";
import { ApiService } from "src/app/Core/Http/api.service";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { User } from "src/app/Core/Model/user.model";
import { PostPreview } from "src/app/Core/Model/post-preview.model";

@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"],
})
export class HomeComponent implements OnInit, OnDestroy {
  destroy: Subject<Boolean> = new Subject();

  user: User;
  postPreviews: Array<PostPreview>;

  constructor(
    private userService: UserService,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.postPreviews = {} as Array<PostPreview>;

    this.userService.user.pipe(takeUntil(this.destroy)).subscribe((user) => {
      this.user = user as User;
    });

    this.apiService.get("/posts", null).subscribe(
      (data) => {
        this.postPreviews = data["result"] as Array<PostPreview>;
      },
      (err) => {
        console.log(err);
      }
    );
  }

  ngOnDestroy(): void {
    this.destroy.next(true);
    this.destroy.unsubscribe();
  }
}
