import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";

@Component({
  selector: "app-subreddit",
  templateUrl: "./subreddit.component.html",
  styleUrls: ["./subreddit.component.scss"],
})
export class SubredditComponent implements OnInit {
  subreddit: string;

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.subreddit = this.route.snapshot.params.subreddit;
    console.log(this.subreddit);
  }
}
