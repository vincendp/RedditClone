import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  ElementRef,
} from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { ApiService } from "src/app/Core/Http/api.service";
import { Subreddit } from "src/app/Core/Model/subreddit.model";
import { UserService } from "src/app/Core/Services/user.service";
import { User } from "src/app/Core/Model/user.model";
import { throwError, Subject } from "rxjs";
import { PostPreview } from "src/app/Core/Model/post-preview.model";
import { switchMap, catchError, takeUntil } from "rxjs/operators";
import { PostType } from "src/app/Core/Model/post-type.enum";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { UtilityService } from "src/app/Core/Services/utility.service";

@Component({
  selector: "app-subreddit",
  templateUrl: "./subreddit.component.html",
  styleUrls: ["./subreddit.component.scss"],
})
export class SubredditComponent implements OnInit, OnDestroy {
  @ViewChild("createTextButton", { read: ElementRef, static: false })
  createTextButton: ElementRef;
  @ViewChild("createImageButton", { read: ElementRef, static: false })
  createImageButton: ElementRef;
  @ViewChild("createLinkButton", { read: ElementRef, static: false })
  createLinkButton: ElementRef;
  @ViewChild("createPostContent", { read: ElementRef, static: false })
  createPostContent: ElementRef;

  PostType = PostType;
  createPostType: PostType;
  createPostForms: { [key: number]: FormGroup };
  createPostForm: FormGroup;

  @ViewChild("fileUpload", { read: ElementRef, static: false })
  fileUpload: ElementRef;
  fileSrc: string | ArrayBuffer;

  destroy: Subject<Boolean> = new Subject();

  subreddit: Subreddit;
  user: User;
  postPreviews: Array<PostPreview>;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private userService: UserService,
    private utilityService: UtilityService,
    private formBuilder: FormBuilder,
    private elem: ElementRef
  ) {}

  ngOnInit(): void {
    let subredditName = this.route.snapshot.params.subreddit;
    this.userService.user.pipe(takeUntil(this.destroy)).subscribe((user) => {
      this.user = user as User;
    });

    this.apiService
      .get(`/subreddits/${subredditName}`, null)
      .pipe(
        switchMap((subreddit) => {
          this.subreddit = subreddit["result"] as Subreddit;
          console.log(this.subreddit);

          return this.apiService.get(
            `/posts/subreddits/${this.subreddit.id}`,
            null
          );
        })
      )
      .subscribe(
        (postPreviews) => {
          this.postPreviews = postPreviews["result"] as Array<PostPreview>;
          console.log(this.postPreviews);
        },
        (err) => {
          if (err.status == 404) {
            this.router.navigateByUrl("/error/not-found");
          }
        }
      );

    this.utilityService.documentClickTarget
      .pipe(takeUntil(this.destroy))
      .subscribe((data) => {
        this.onDocumentClick(data);
      });

    this.initFormGroup();
  }

  ngOnDestroy(): void {
    this.destroy.next(true);
    this.destroy.unsubscribe();
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

  onCreatePost() {
    this.createPostForm.controls.post_type.setValue(this.createPostType);
    let postForm = this.createPostForm.value;

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
        this.router.navigate([`${data.result.id}`], {
          relativeTo: this.route,
        });
      },
      (err) => {
        console.log(err);
      }
    );
  }

  setPostType(postType: PostType): void {
    if (!this.user || !this.user.id) {
      alert("Error: Not authenticated");
      this.userService.setRedirectUrl(this.router.routerState.snapshot.url);
      this.router.navigateByUrl("/login");
      return;
    }
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
}
