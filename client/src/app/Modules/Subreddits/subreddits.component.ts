import { Component, OnInit } from "@angular/core";
import { ApiService } from "src/app/Core/Http/api.service";

@Component({
  selector: "app-subreddits",
  templateUrl: "./subreddits.component.html",
  styleUrls: ["./subreddits.component.scss"],
})
export class SubredditsComponent implements OnInit {
  subreddits: Object[];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.loadSubreddits();
  }

  loadSubreddits(): void {
    this.apiService.get("/subreddits", null).subscribe(
      (data) => {
        this.subreddits = data["data"];
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
