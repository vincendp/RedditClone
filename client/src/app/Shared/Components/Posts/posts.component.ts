import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  OnDestroy,
} from "@angular/core";
import { PostType } from "src/app/Core/Model/post-type.enum";
import { UtilityService } from "src/app/Core/Services/utility.service";
import { Subscription } from "rxjs";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";

@Component({
  selector: "app-posts",
  templateUrl: "./posts.component.html",
  styleUrls: ["./posts.component.scss"],
})
export class PostsComponent implements OnInit, OnDestroy {
  @ViewChild("createTextPost", { read: ElementRef, static: false })
  createTextPost: ElementRef;
  @ViewChild("createImagePost", { read: ElementRef, static: false })
  createImagePost: ElementRef;
  @ViewChild("createLinkPost", { read: ElementRef, static: false })
  createLinkPost: ElementRef;
  @ViewChild("createPostContent", { read: ElementRef, static: false })
  createPostContent: ElementRef;
  documentClickSubscription: Subscription;

  PostType = PostType;
  createPostType: PostType;
  createPostForm: FormGroup;

  constructor(
    private utilityService: UtilityService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.documentClickSubscription = this.utilityService.documentClickTarget.subscribe(
      (data) => {
        this.onDocumentClick(data);
      },
      (err) => {
        console.log(err);
      }
    );
    this.createPostForm = this.formBuilder.group({
      username: ["", Validators.required],
      password: ["", Validators.required],
    });
  }

  ngOnDestroy(): void {
    this.documentClickSubscription.unsubscribe();
  }

  onClickCreatePost(postType: PostType): void {
    this.createPostType = postType;
  }

  onCloseCreatePost(): void {
    this.createPostType = null;
  }

  onDocumentClick(target: any): void {
    if (
      !this.createPostContent.nativeElement.contains(target) &&
      !this.createTextPost.nativeElement.contains(target) &&
      !this.createImagePost.nativeElement.contains(target) &&
      !this.createLinkPost.nativeElement.contains(target)
    ) {
      this.onCloseCreatePost();
    }
  }
}
