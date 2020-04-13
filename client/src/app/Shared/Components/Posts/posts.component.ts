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
  @ViewChild("createTextButton", { read: ElementRef, static: false })
  createTextButton: ElementRef;
  @ViewChild("createImageButton", { read: ElementRef, static: false })
  createImageButton: ElementRef;
  @ViewChild("createLinkButton", { read: ElementRef, static: false })
  createLinkButton: ElementRef;
  @ViewChild("createPostContent", { read: ElementRef, static: false })
  createPostContent: ElementRef;
  documentClickSubscription: Subscription;

  PostType = PostType;
  createPostType: PostType;
  createPostForms: { [key: number]: FormGroup };
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

    this.initFormGroup();
  }

  ngOnDestroy(): void {
    this.documentClickSubscription.unsubscribe();
  }

  initFormGroup(): void {
    const urlPattern = /^(?:http(s)?:\/\/)?[\w.-]+(?:\.[\w\.-]+)+[\w\-\._~:/?#[\]@!\$&'\(\)\*\+,;=.]+$/;

    this.createPostForms = {};
    this.createPostForms[PostType.TEXT] = this.formBuilder.group({
      title: ["", Validators.required],
      description: [""],
    });
    this.createPostForms[PostType.IMAGE] = this.formBuilder.group({
      title: ["", Validators.required],
      image: [""],
    });
    this.createPostForms[PostType.LINK] = this.formBuilder.group({
      title: ["", Validators.required],
      link: ["", Validators.required, Validators.pattern(urlPattern)],
    });

    this.createPostForm = this.createPostForms[PostType.TEXT];
  }

  onClickCreatePost(postType: PostType): void {
    this.createPostType = postType;
    this.createPostForm = this.createPostForms[postType];
  }

  onCloseCreatePost(): void {
    this.createPostType = null;
    this.initFormGroup();
  }

  onDocumentClick(target: any): void {
    if (
      !this.createPostContent.nativeElement.contains(target) &&
      !this.createTextButton.nativeElement.contains(target) &&
      !this.createImageButton.nativeElement.contains(target) &&
      !this.createLinkButton.nativeElement.contains(target)
    ) {
      this.onCloseCreatePost();
    }
  }
}
