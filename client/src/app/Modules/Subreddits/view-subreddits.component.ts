import { Component, OnInit } from "@angular/core";
import { ApiService } from "src/app/Core/Http/api.service";
import { Subreddit } from "src/app/Core/Model/subreddit.model";

@Component({
  selector: "app-view-subreddits",
  templateUrl: "./view-subreddits.component.html",
  styleUrls: ["./view-subreddits.component.scss"],
})
export class ViewSubredditsComponent implements OnInit {
  subreddits: Array<Subreddit>;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadSubreddits();
  }

  loadSubreddits(): void {
    this.apiService.get("/subreddits", null).subscribe(
      (data) => {
        this.subreddits = data["result"] as Array<Subreddit>;
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
