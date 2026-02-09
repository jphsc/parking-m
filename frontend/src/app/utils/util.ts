export abstract class Utils {
  public static formatarPlaca(placa: string):string {
    return placa.replaceAll(/[^a-zA-Z0-9]/g, '').toUpperCase().substring(0,7);
  }

  public static uppercaseOnly(valor: string): string {
    return valor.toUpperCase();
  }
}
