import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  OnDestroy,
  Output,
  EventEmitter,
  Input,
} from "@angular/core";
import { environment } from "src/environments/environment";
import { PostType } from "src/app/Core/Model/post-type.enum";
import { UtilityService } from "src/app/Core/Services/utility.service";
import { Subscription } from "rxjs";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { DomSanitizer } from "@angular/platform-browser";
import { PostPreview } from "src/app/Core/Model/post-preview.model";
import { ApiService } from "src/app/Core/Http/api.service";

@Component({
  selector: "app-posts",
  templateUrl: "./posts.component.html",
  styleUrls: ["./posts.component.scss"],
})
export class PostsComponent implements OnInit, OnDestroy {
  @Input() postPreviews: Array<PostPreview>;
  @Output() createPostEvent: EventEmitter<{}> = new EventEmitter<{}>();

  @ViewChild("createTextButton", { read: ElementRef, static: false })
  createTextButton: ElementRef;
  @ViewChild("createImageButton", { read: ElementRef, static: false })
  createImageButton: ElementRef;
  @ViewChild("createLinkButton", { read: ElementRef, static: false })
  createLinkButton: ElementRef;
  @ViewChild("createPostContent", { read: ElementRef, static: false })
  createPostContent: ElementRef;

  apiUrl: string = environment.apiUrl;

  PostType = PostType;
  createPostType: PostType;
  createPostForms: { [key: number]: FormGroup };
  createPostForm: FormGroup;

  @ViewChild("fileUpload", { read: ElementRef, static: false })
  fileUpload: ElementRef;
  fileSrc: string | ArrayBuffer;

  documentClickSubscription: Subscription;

  constructor(
    private utilityService: UtilityService,
    private formBuilder: FormBuilder,
    private elem: ElementRef,
    private sanitizer: DomSanitizer,
    private apiService: ApiService
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

  ngAfterViewInit() {
    let elements = this.elem.nativeElement.querySelectorAll(".lazy");
    elements.forEach((element) => {
      console.log(element);
    });
  }

  initFormGroup(): void {
    const urlPattern =
      "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    this.createPostForms = {};
    this.createPostForms[PostType.TEXT] = this.formBuilder.group({
      title: ["", Validators.required],
      description: [""],
      post_type: [""],
    });
    this.createPostForms[PostType.IMAGE] = this.formBuilder.group({
      title: ["", Validators.required],
      image: ["", Validators.required],
      post_type: [""],
    });
    this.createPostForms[PostType.LINK] = this.formBuilder.group({
      title: ["", Validators.required],
      link: [
        "",
        [Validators.required, Validators.pattern(new RegExp(urlPattern))],
      ],
      post_type: [""],
    });

    this.createPostForm = this.createPostForms[PostType.TEXT];
  }

  setPostType(postType: PostType): void {
    this.createPostType = postType;
    this.createPostForm = this.createPostForms[postType];
  }

  closePostForm(): void {
    this.createPostType = null;
    this.fileSrc = null;
    this.initFormGroup();
  }

  onDocumentClick(target: any): void {
    if (
      !this.createPostContent.nativeElement.contains(target) &&
      !this.createTextButton.nativeElement.contains(target) &&
      !this.createImageButton.nativeElement.contains(target) &&
      !this.createLinkButton.nativeElement.contains(target)
    ) {
      this.closePostForm();
    }
  }

  onClickUploadButton(): void {
    this.fileUpload.nativeElement.click();
  }

  onChangeFile(): void {
    const fileReader = new FileReader();

    if (this.fileUpload.nativeElement.files) {
      fileReader.readAsDataURL(this.fileUpload.nativeElement.files[0]);
    }

    fileReader.onload = (e) => {
      this.fileSrc = fileReader.result;
      this.createPostForm.controls.image.setValue(
        this.fileUpload.nativeElement.files[0]
      );
    };
  }

  deleteFile(): void {
    this.fileSrc = null;
    this.createPostForm.controls.image.setValue(null);
  }

  createPost(): void {
    this.createPostForm.controls.post_type.setValue(this.createPostType);
    this.createPostEvent.emit(this.createPostForm.value);
  }

  getSanitizedURL(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  hi() {
    console.log("IN VIEW");
  }

  // createVoteForPost(postPreview: PostPreview, vote: boolean) {
  //   this.apiService
  //     .post(
  //       "/votes/posts",
  //       {
  //         post_id: postPreview.post_id,
  //         user_id: this.user.id,
  //         vote: vote,
  //       },
  //       {}
  //     )
  //     .subscribe(
  //       () => {
  //         postPreview.votes += vote ? 1 : -1;
  //         postPreview.user_voted_for_post = vote ? 1 : -1;
  //       },
  //       (err) => {
  //         console.log(err);
  //       }
  //     );
  // }

  // updateVoteForPost(postPreview: PostPreview, vote: boolean) {
  //   this.apiService
  //     .put(
  //       "/votes/posts",
  //       {
  //         post_id: postPreview.post_id,
  //         user_id: this.user.id,
  //         vote: vote,
  //       },
  //       {}
  //     )
  //     .subscribe(
  //       () => {
  //         postPreview.votes += vote ? 2 : -2;
  //         postPreview.user_voted_for_post = vote ? 1 : -1;
  //       },
  //       (err) => {
  //         console.log(err);
  //       }
  //     );
  // }

  // deleteVoteForPost(postPreview: PostPreview, vote: boolean) {
  //   this.apiService
  //     .delete(
  //       "/votes/posts",
  //       {
  //         post_id: postPreview.post_id,
  //         user_id: this.user.id,
  //       },
  //       {}
  //     )
  //     .subscribe(
  //       () => {
  //         postPreview.votes += vote ? -1 : 1;
  //         postPreview.user_voted_for_post = 0;
  //       },
  //       (err) => {
  //         console.log(err);
  //       }
  //     );
  // }
}
