import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  OnDestroy,
  Output,
  EventEmitter,
} from "@angular/core";
import { PostType } from "src/app/Core/Model/post-type.enum";
import { UtilityService } from "src/app/Core/Services/utility.service";
import { Subscription } from "rxjs";
import {
  FormBuilder,
  Validators,
  FormGroup,
  FormControl,
} from "@angular/forms";

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

  @ViewChild("fileUpload", { read: ElementRef, static: false })
  fileUpload: ElementRef;
  fileSrc: string | ArrayBuffer;

  @Output() createPostEvent: EventEmitter<{}> = new EventEmitter<{}>();

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
    const urlPattern =
      "^(?:http(s)?:\\/\\/)?[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\]@!\\$&'\\(\\)\\*\\+,;=.]+$";
    this.createPostForms = {};
    this.createPostForms[PostType.TEXT] = this.formBuilder.group({
      title: ["", Validators.required],
      description: [""],
      image: [""],
      link: [""],
      post_type: [""],
    });
    this.createPostForms[PostType.IMAGE] = this.formBuilder.group({
      title: ["", Validators.required],
      description: [""],
      image: ["", Validators.required],
      link: [""],
      post_type: [""],
    });
    this.createPostForms[PostType.LINK] = this.formBuilder.group({
      title: ["", Validators.required],
      description: [""],
      image: [""],
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

  createPost() {
    this.createPostForm.controls.post_type.setValue(this.createPostType);
    this.createPostEvent.emit(this.createPostForm.value);
  }
}
