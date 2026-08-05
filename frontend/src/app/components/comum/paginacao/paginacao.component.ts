import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-paginacao',
  templateUrl: './paginacao.component.html',
  styleUrls: ['./paginacao.component.css']
})
export class PaginacaoComponent {

  @Input() paginaAtual!: number;
  @Input() qtdPaginas: number[] = [];
  @Output() paginar: EventEmitter<number> = new EventEmitter<number>();

  public paginarRegistros(page: number): void {
    this.paginar.emit(page);
  }
}

