import { Component, OnInit, OnDestroy } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { ApiService } from "src/app/Core/Http/api.service";
import { HttpParams } from "@angular/common/http";
import { Subreddit } from "src/app/Core/Model/subreddit.model";
import { UserService } from "src/app/Core/Services/user.service";
import { User } from "src/app/Core/Model/user.model";
import { take } from "rxjs/operators";
import { Subscription } from "rxjs";

import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { HttpHeaders, HttpClient } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";

@Component({
  selector: "app-subreddit",
  templateUrl: "./subreddit.component.html",
  styleUrls: ["./subreddit.component.scss"],
})
export class SubredditComponent implements OnInit, OnDestroy {
  userSubscription: Subscription;
  subreddit: Subreddit;
  user: User;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private userService: UserService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    let subredditName = this.route.snapshot.params.subreddit;
    this.apiService.get(`/subreddits/${subredditName}`, null).subscribe(
      (data) => {
        this.subreddit = data["result"] as Subreddit;
      },
      (err) => {}
    );
    this.userSubscription = this.userService.user.subscribe(
      (data) => {
        this.user = data as User;
      },
      (err) => {}
    );
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

  onCreatePost(postForm: any) {
    if (this.user && this.user.id) {
      postForm["user_id"] = this.user.id;
    }
    if (this.subreddit && this.subreddit.id) {
      postForm["subreddit_id"] = this.subreddit.id;
    }

    let formData = new FormData();
    Object.keys(postForm).forEach((key) => {
      formData.append(key, postForm[key]);
    });

    console.log(postForm);

    let options = {
      headers: { Accept: "application/json" },
    };

    this.apiService.postFormData("/posts", formData, options).subscribe(
      (data) => {
        console.log(data);
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
