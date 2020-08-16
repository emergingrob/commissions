//
//  CurrencyField.swift
//  commissions
//
//  Created by Rob Miller on 2/29/20.
//  Copyright © 2020 Rob Miller. All rights reserved.
//

import UIKit

class CurrencyField: UITextField {
  static let max_digits = 15
  static var locale:Locale!
  
  var value:Float {
    get{
      Formatter.currency.locale = Locale.current
      let divider:Double = pow(Double(10), Double(Formatter.currency.maximumFractionDigits))
      
      if let t = text{
        return Float(t.numbers.integer) / Float(divider)
      }else{
        return 0.0
      }
    }
    set(newVal){
      Formatter.currency.locale = Locale.current
      text = Formatter.currency.string(from: NSNumber(value: newVal))
    }
  }
  
  override func awakeFromNib() {
    super.awakeFromNib()
    addTarget(self, action: #selector(editingChanged), for: .editingChanged)
    keyboardType = .numberPad
    textAlignment = .right
    editingChanged()
  }
    @objc func editingChanged() {
    let max = String(string.numbers.characters.prefix(CurrencyField.max_digits))
    
      Formatter.currency.locale = Locale.current
      let divider:Double = pow(Double(10), Double(Formatter.currency.maximumFractionDigits))
      text = Formatter.currency.string(from: (Double(max.numbers.integer) / divider) as NSNumber)
      self.value = (Float(max.numbers.integer) / Float(divider))
  }
}

struct Formatter {
  static let currency = NumberFormatter(numberStyle: .currency)
}

extension UITextField {
  var string: String { return text ?? "" }
}

extension String {
  var numbers: String { return components(separatedBy: Numbers.characterSet.inverted).joined() }
  var integer: Int { return Int(numbers) ?? 0 }
}

struct Numbers { static let characterSet = CharacterSet(charactersIn: "0123456789") }

extension NumberFormatter {
  convenience init(numberStyle: NumberFormatter.Style) {
    self.init()
    self.numberStyle = numberStyle
  }
}
