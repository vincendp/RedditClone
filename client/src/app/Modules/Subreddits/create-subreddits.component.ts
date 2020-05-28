import { Component, OnInit } from "@angular/core";
import { ApiService } from "src/app/Core/Http/api.service";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";

@Component({
  selector: "app-create-subreddits",
  templateUrl: "./create-subreddits.component.html",
  styleUrls: ["./create-subreddits.component.scss"],
})
export class CreateSubredditsComponent implements OnInit {
  subredditForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.subredditForm = this.formBuilder.group({
      name: ["", Validators.required],
    });
  }

  onSubmitCreateSubreddit() {
    this.apiService
      .post("/subreddits", this.subredditForm.value, {})
      .subscribe((data) => {});
  }
}
