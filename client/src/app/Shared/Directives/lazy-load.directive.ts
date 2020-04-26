import {
  Directive,
  OnInit,
  OnDestroy,
  EventEmitter,
  Output,
  ElementRef,
} from "@angular/core";
import {
  LazyTarget,
  IntersectionService,
} from "src/app/Core/Services/intersection.service";

@Directive({
  selector: "[lazyLoad]",
})
export class LazyLoadDirective implements OnInit, OnDestroy, LazyTarget {
  @Output() public loadedElement: EventEmitter<any> = new EventEmitter();

  element: Element;

  constructor(
    private elementRef: ElementRef,
    private intersectionService: IntersectionService
  ) {
    this.element = this.elementRef.nativeElement;
  }

  ngOnInit(): void {
    this.intersectionService.addTarget(this);
  }

  ngOnDestroy(): void {
    if (this.intersectionService) this.intersectionService.removeTarget(this);
    this.loadedElement.unsubscribe();
  }

  emitLoadedElement(): void {
    this.loadedElement.emit();
    this.intersectionService.removeTarget(this);
    this.intersectionService = null;
  }
}
